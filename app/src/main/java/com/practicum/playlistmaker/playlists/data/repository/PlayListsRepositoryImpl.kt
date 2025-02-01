package com.practicum.playlistmaker.playlists.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.provider.OpenableColumns
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.favorites.data.converters.TrackDbConverter
import com.practicum.playlistmaker.favorites.data.db.AppDatabase
import com.practicum.playlistmaker.favorites.domain.repository.FavoriteTracksRepository
import com.practicum.playlistmaker.playlists.data.converters.PlaylistDbConverter
import com.practicum.playlistmaker.playlists.data.converters.PlaylistsTrackDbConverter
import com.practicum.playlistmaker.playlists.data.db.PlaylistsTrackEntity
import com.practicum.playlistmaker.playlists.domain.models.Playlist
import com.practicum.playlistmaker.playlists.domain.repository.PlaylistsRepository
import com.practicum.playlistmaker.playlists.ui.PlaylistTracksState
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class PlayListsRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbConverter: PlaylistDbConverter,
    private val playlistsTrackDbConverter: PlaylistsTrackDbConverter,

    private val context: Context


) : PlaylistsRepository {
    override suspend fun addPlaylist(playlist: Playlist) {
        playlist.id = 0
        appDatabase.playlistDao().insertPlaylist(playlistDbConverter.map(playlist))
    }

    override suspend fun updatePlayList(playlist: Playlist) {
        appDatabase.playlistDao().updatePlaylist(playlistDbConverter.map(playlist))
    }

    override suspend fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = appDatabase.playlistDao().getPlaylists()
        emit(playlists.map { playlist -> playlistDbConverter.map(playlist) })
    }

    override suspend fun addTrackToPlayList(track: Track, playlist: Playlist) {


        //playlist.tracksIds.add(track.trackId)


        playlist.tracksIds.add(0, track.trackId)



        playlist.tracksNum++

        val recs = appDatabase.playlistDao().updatePlaylist(playlistDbConverter.map(playlist))

        appDatabase.playlistsTrackDao().insertPlaylistsTrack(playlistsTrackDbConverter.map(track))
    }





    override suspend fun deletePlaylist(playlist: Playlist?){

       // var tracks: List<Track> = emptyList()




        var tracks: MutableList<PlaylistsTrackEntity> = mutableListOf()


        val allTracks = appDatabase.playlistsTrackDao().getAllTracks()


        for (trackId in playlist!!.tracksIds) {

            for (track in allTracks) {

                if (trackId == track.trackId) {


                    tracks.add(track)


                    break

                }

            }

        }






     /*  GlobalScope.launch {

           getPlaylistTracks(playlist)
               //.collect{ result -> tracks = result }
               .collect { result ->
                   removeTracksFromPlayListsTracks(result)
                   appDatabase.playlistDao().deletePlaylist(playlistDbConverter.map(playlist))


               }

       } */




        //val tracksMapped = tracks.map { track -> playlistsTrackDbConverter.map(track) }


        appDatabase.playlistDao().deletePlaylist(playlistDbConverter.map(playlist))


        removeTracksFromPlayListsTracks(tracks.map { track -> playlistsTrackDbConverter.map(track) })







        /*val allTracks = appDatabase.playlistsTrackDao().getAllTracks()

        for (trackId in playlist.tracksIds) {

            for (track in allTracks) {

                if (trackId == track.trackId) {


                    removeTrackFromPlayListsTracks(playlistsTrackDbConverter.map(track))


                    break

                }

            }

        }*/




        //appDatabase.playlistDao().deletePlaylist(playlistDbConverter.map(playlist))




    }



    override suspend fun getPlaylistTracks(playlist: Playlist?): Flow<List<Track>> = flow {


        var tracks: MutableList<PlaylistsTrackEntity> = mutableListOf()


        val allTracks = appDatabase.playlistsTrackDao().getAllTracks()


        for (trackId in playlist!!.tracksIds) {

            for (track in allTracks) {

                if (trackId == track.trackId) {


                    tracks.add(track)


                    break

                }

            }

        }



        val tracksMapped = tracks.map { track -> playlistsTrackDbConverter.map(track) }



        val favoriteTracksIds = appDatabase.favoriteTrackDao().getTracksIds()

        for (track in tracksMapped) {
            track.isFavorite = false
            if (track.trackId in favoriteTracksIds)
                track.isFavorite = true
        }

        emit(tracksMapped)

    }










    override suspend fun removeTrackFromPlayList(track: Track, playlist: Playlist?) {

        val tracks = listOf(track)

        playlist!!.tracksIds.remove(track.trackId)
        playlist!!.tracksNum--

        val recs = appDatabase.playlistDao().updatePlaylist(playlistDbConverter.map(playlist))


        removeTracksFromPlayListsTracks(tracks)


        //removeTrackFromPlayListsTracks(track)


    }





    suspend fun removeTracksFromPlayListsTracks(tracks: List<Track>) {

        var playlistFound = false


        val playlists = appDatabase.playlistDao().getPlaylists()
            .map { playlist -> playlistDbConverter.map(playlist) }


        for(track in tracks) {

            for (playlist in playlists) {


                if (track.trackId in playlist.tracksIds) {


                    playlistFound = true

                    break

                }

            }

            if (!playlistFound) {


                appDatabase.playlistsTrackDao()
                    .deletePlaylistsTrack(playlistsTrackDbConverter.map(track))


                playlistFound = false

            }

        }
    }


    /*suspend fun removeTrackFromPlayListsTracks(track: Track) {

        var playlistFound = false


        val playlists = appDatabase.playlistDao().getPlaylists()
            .map { playlist -> playlistDbConverter.map(playlist) }

        for (playlist in playlists) {

            if (track.trackId in playlist.tracksIds) {


                playlistFound = true

                break

            }


        }


        if (!playlistFound) {


            appDatabase.playlistsTrackDao()
                .deletePlaylistsTrack(playlistsTrackDbConverter.map(track))


        }


    }*/






    @SuppressLint("Range")
    private fun getFileNameFromUri(context: Context, uri: Uri): String? {
        val fileName: String?
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.moveToFirst()
        fileName = cursor?.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
        cursor?.close()
        return fileName
    }


    override suspend fun saveImageToPrivateStorage(uri: Uri) : String{
        //создаём экземпляр класса File, который указывает на нужный каталог
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "playlistsimages")
        //создаем каталог, если он не создан
        if (!filePath.exists()){
            filePath.mkdirs()
        }

        val fileName = getFileNameFromUri(context,uri)

        val file = File(filePath, fileName)
        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)

        return file.getAbsolutePath()
    }






}

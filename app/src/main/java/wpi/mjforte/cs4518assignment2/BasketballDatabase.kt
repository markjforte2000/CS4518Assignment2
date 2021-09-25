package wpi.mjforte.cs4518assignment2

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [BasketballGame::class], version = 1)
@TypeConverters(BasketballConverter::class)
abstract class BasketballDatabase : RoomDatabase() {

    abstract fun basketballDAO(): BasketballDAO

}

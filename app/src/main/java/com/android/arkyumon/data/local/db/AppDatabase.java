/*
 *  Copyright (C) 2019
 *
 *  Licensed under the MIT License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      https://spit.com/license/apache-v2
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License
 */

package com.android.arkyumon.data.local.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.android.arkyumon.data.local.db.dao.OptionDao;
import com.android.arkyumon.data.local.db.dao.PotholesDao;
import com.android.arkyumon.data.local.db.dao.QuestionDao;
import com.android.arkyumon.data.local.db.dao.UserDao;
import com.android.arkyumon.data.model.db.Option;
import com.android.arkyumon.data.model.db.Potholes;
import com.android.arkyumon.data.model.db.Question;
import com.android.arkyumon.data.model.db.User;
import com.android.arkyumon.utils.LocationConverter;

/**
 * Created by CodersClan on 07/07/17.
 */
@TypeConverters(LocationConverter.class)
@Database(entities = {User.class, Question.class, Option.class, Potholes.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {

    public abstract OptionDao optionDao();

    public abstract QuestionDao questionDao();

    public abstract UserDao userDao();

    public abstract PotholesDao potholesDao();
}

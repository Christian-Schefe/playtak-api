#!/bin/bash
# set the playtakdb folder to the parent directory variable
dbPath="../../playtakdb"

# check if playtakdb folder exists
if [ ! -d "$dbPath" ]; then
    mkdir "$dbPath"
fi

# remove existing databases if they exist
if [ -f "$dbPath/players.db" ]; then
    rm "$dbPath/players.db"
fi

if [ -f "$dbPath/games.db" ]; then
    rm "$dbPath/games.db"
fi

playersdb="$dbPath/players.db"
gamesdb="$dbPath/games.db"

# create db, tables
echo "CREATE TABLE players (
    id INT PRIMARY KEY,
    name VARCHAR(20),
    password VARCHAR(50),
    email VARCHAR(50),
    rating REAL DEFAULT 1000,
    boost REAL DEFAULT 750,
    ratedgames INT DEFAULT 0,
    maxrating REAL DEFAULT 1000,
    ratingage REAL DEFAULT 0,
    ratingbase INT DEFAULT 0,
    unrated INT DEFAULT 0,
    isbot INT DEFAULT 0,
    fatigue TEXT DEFAULT '{}',
    is_admin INT DEFAULT 0,
    is_mod INT DEFAULT 0,
    is_gagged INT DEFAULT 0,
    is_banned INT DEFAULT 0,
    participation_rating INT DEFAULT 1000
);" | sqlite3 "$playersdb"

echo "CREATE TABLE games (
    id INTEGER PRIMARY KEY,
    date INT,
    size INT,
    player_white VARCHAR(20),
    player_black VARCHAR(20),
    notation TEXT,
    result VARCHAR(10),
    timertime INT DEFAULT 0,
    timerinc INT DEFAULT 0,
    rating_white INT DEFAULT 1000,
    rating_black INT DEFAULT 1000,
    unrated INT DEFAULT 0,
    tournament INT DEFAULT 0,
    komi INT DEFAULT 0,
    pieces INT DEFAULT -1,
    capstones INT DEFAULT -1,
    rating_change_white INT DEFAULT 0,
    rating_change_black INT DEFAULT 0,
    extra_time_amount INT DEFAULT 0,
    extra_time_trigger INT DEFAULT 0
);" | sqlite3 "$gamesdb"

package spacetime

import "github.com/p2hacks2022/post-team05/internal/model"

// spacetimesにinsertする
func Post(time string, latitude, longitude, altitude float64, objId int) error {
	// dbmap初期化
	dbmap, err := model.InitDb()
	if err != nil {
		return err
	}
	defer dbmap.Db.Close()

	// dbmapにテーブル名登録
	dbmap.AddTableWithName(SpaceTime{}, "spacetimes")

	// Insert
	spaceTimes := &SpaceTime{
		Time:      time,
		Latitude:  latitude,
		Longitude: longitude,
		Altitude:  altitude,
		ObjId:     objId,
	}
	err = dbmap.Insert(spaceTimes)
	if err != nil {
		return err
	}

	return nil
}

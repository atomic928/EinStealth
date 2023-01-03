package spacetime

type SpaceTime struct {
	Id        int     `db:"id"`
	Time      string  `db:"time"`
	Latitude  float32 `db:"latitude"`
	Longitude float32 `db:"longitude"`
	Altitude  float32 `db:"altitude"`
	ObjId     int     `db:"obj_id"`
}

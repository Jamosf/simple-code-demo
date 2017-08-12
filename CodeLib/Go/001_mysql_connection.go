func mysql_connection(){
	db, err := sql.Open("mysql", "root:password123@tcp(192.168.59.103:3306)/mysql")
	if err != nil {
		logs.Error(err.Error())
	}
	err = db.Ping()
	if err != nil {
		logs.Debug("wrong")
	}
	rows, queryerr := db.Query("select host from user")
	if queryerr != nil {
		logs.Debug("query fail")
	}
	defer db.Close()
	var name string
	for rows.Next() {
		err = rows.Scan(&name)
		if err == nil {
			fmt.Printf("name is:%s\n", name)
		}
	}
}


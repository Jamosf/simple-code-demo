package main

import (
	"os"
	"os/signal"
	"syscall"
)

var (
	sigs chan os.Signal
)

func init() {
	sigs = make(chan os.Signal, 1)
	signal.Notify(sigs, syscall.SIGINT, syscall.SIGTERM)
}

func main() {
	//scrapy()
	//defer so.destory()

	//so.autoReadPages()
	//so.searchBaidu("suosuo")

	new(scrapy).scrapy()

	<-sigs
}

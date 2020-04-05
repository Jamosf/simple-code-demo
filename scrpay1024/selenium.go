package main

import (
	"fmt"
	"log"
	"time"

	"github.com/tebeka/selenium"
)

var (
	so = &seleniumOperator{}
)

const (
	seleniumPath = `C:\Program Files\chromedriver\selenium-server-standalone-3.141.59.jar`
	port         = 8080

	// 搜索网址
	baiduUrl = "https://www.baidu.com"
)

type seleniumOperator struct {
	service *selenium.Service
	wd      selenium.WebDriver
}

func (s *seleniumOperator) destory() {
	s.wd.Close()
	s.service.Stop()
	log.Println("realse all resource success")
}

func (s *seleniumOperator) init() {
	var err error
	if s.service, err = selenium.NewSeleniumService(seleniumPath, port); err != nil {
		panic(err)
	}

	if s.wd, err = selenium.NewRemote(selenium.Capabilities{"browserName": "chrome"}, fmt.Sprintf("http://localhost:%d/wd/hub", port)); err != nil {
		panic(err)
	}
}

func (s *seleniumOperator) searchBaidu(searchWord string) {
	if err := s.wd.Get(baiduUrl); err != nil {
		log.Printf("get html fail, err: %v", err)
		return
	}
	s.wd.SetImplicitWaitTimeout(4)
	input, err := s.wd.FindElement(selenium.ByCSSSelector, "#kw")
	if err != nil {
		log.Printf("find input css fail, err: %v", err)
		return
	}
	input.SendKeys(searchWord)
	search, err := s.wd.FindElement(selenium.ByCSSSelector, "#su")
	if err != nil {
		log.Printf("find click css fail, err: %v", err)
		return
	}
	search.Click()
}

func (s *seleniumOperator) autoReadPages() {
	log.Println("auto reading start, enjoy!!!")
	if err := s.wd.Get(dailySet); err != nil {
		log.Printf("get 1024 html fail, err: %v", err)
		return
	}
	s.wd.SetImplicitWaitTimeout(4)
	s.wd.SetPageLoadTimeout(10 * time.Second)
	subUrlBtns, err := s.wd.FindElements(selenium.ByCSSSelector, "td h3 a")
	if err != nil {
		log.Printf("get suburlbtn fail, err: %v", err)
		return
	}
	btnCnt := len(subUrlBtns)
	finish := make(chan struct{}, 1)
	for i := 0; i < btnCnt; i++ {
		if i <= 3 {
			continue
		}
		subUrlBtns[i].Click()
		go func(wd selenium.WebDriver) {
			for cnt := 0; cnt < 10; cnt++ {
				wd.KeyDown(selenium.SpaceKey)
				time.Sleep(100 * time.Millisecond)
				wd.KeyUp(selenium.SpaceKey)
				time.Sleep(2 * time.Second)
			}
			finish <- struct{}{}
		}(s.wd)
		if err := s.wd.SetPageLoadTimeout(20 * time.Second); err != nil {
			log.Println("page load timeout set fail")
			return
		}
		<-finish
		s.wd.Back()
		time.Sleep(1 * time.Second)
		subUrlBtns, _ = s.wd.FindElements(selenium.ByCSSSelector, "td h3 a")
	}
}

package main

import (
	"io"
	"io/ioutil"
	"log"
	"net/http"
	"os"
	"strconv"
	"strings"

	"github.com/PuerkitoBio/goquery"

	"github.com/jackdanger/collectlinks"
)

const (
	// 网址
	contentHeadUrl = "https://a1.ak1787.rocks/pw/"
	dailySet       = contentHeadUrl + "thread.php?fid=3"
)

func init() {
	fp := &LogFile{fp: "log.txt"}
	log.SetOutput(fp)
}

type LogFile struct {
	fp string
}

func (lf *LogFile) Write(p []byte) (int, error) {
	f, err := os.OpenFile(lf.fp, os.O_CREATE|os.O_APPEND, 0x666)
	defer f.Close()

	if err != nil {
		return -1, err
	}
	return f.Write(p)
}

type scrapy struct {
}

func (s *scrapy) scrapy() {
	log.Println("running...")

	body := s.downloadUrl(dailySet)
	defer body.Close()

	links := collectlinks.All(body)

	for i, link := range links {
		if !strings.Contains(link, "html_data/3/2004") {
			continue
		}
		log.Println(link)
		subBody := s.downloadUrl(contentHeadUrl + link)
		doc, err := goquery.NewDocumentFromReader(subBody)
		if err != nil {
			log.Println(err)
			return
		}
		doc.Find("#read_tpc img").Each(func(j int, ss *goquery.Selection) {
			imgUrl, ok := ss.Attr("src")
			if ok {
				bytes, err := ioutil.ReadAll(s.downloadUrl(imgUrl))
				if err != nil {
					log.Println(err)
					return
				}
				if err = ioutil.WriteFile(strconv.Itoa(i)+"_"+strconv.Itoa(j)+".jpg", bytes, os.ModePerm); err != nil {
					log.Printf("write byte to file fail, err: %v", err)
				}
			}
		})
	}

	log.Println("stopped")
}

func (s *scrapy) downloadUrl(url string) io.ReadCloser {
	client := &http.Client{}
	req, err := http.NewRequest("GET", url, nil)
	if err != nil {
		log.Printf("new request fail, err: %v", err)
	}
	req.Header.Set("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36")

	response, err := client.Do(req)
	if err != nil {
		log.Printf("open main url fail, err:%v", err)
		return nil
	}
	return response.Body
}

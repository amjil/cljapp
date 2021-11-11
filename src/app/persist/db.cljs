(ns app.persist.sqlite
  (:require
   [applied-science.js-interop :as j]
   [cljs-bean.core :as bean]
   ["react-native-fs" :as fs]
   ["react-native-sqlite-storage" :as sqlite]))

(sqlite/enablePromise false)
(def conn
  (sqlite/openDatabase (bean/->js {:name "cand.db" :location "default"})))
  ;; (sqlite/openDatabase (bean/->js {:name "cand.db" :location (str fs/DocumentDirectoryPath "/cand.db")})))
(sqlite/enablePromise true)
(.close conn)
(comment
  sqlite/openDatabase
  conn

  (j/call conn :executeSql "select * from a where giglgc = 'ab'")
  (.then (.executeSql conn "select * from a where giglgc = 'ab'")
  #(js/console.log "result = " %)       )
  )
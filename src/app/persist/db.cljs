(ns app.persist.sqlite
  (:require
   [applied-science.js-interop :as j]
   [cljs-bean.core :as bean]
   ["react-native" :as rn]
   ["react-native-fs" :as fs]
   ["react-native-sqlite-storage" :as sqlite]))

;; (sqlite/enablePromise false)
(sqlite/enablePromise true)

(def conn
  (let [platform    (j/get-in rn [:Platform :OS])
        file-prefix (condp = platform
                      "android" ""
                      "ios" fs/LibraryDirectoryPath)]
    (sqlite/openDatabase 
     (bean/->js
      {:name               "cand.db"
       :createFromLocation (str file-prefix "/LocalDatabase" "/cand.db")}))))

(.close conn)
(comment
  sqlite/openDatabase
  conn
  (str fs/DocumentDirectoryPath "/cand.db")

  (j/call conn :executeSql "select * from a where giglgc = 'ab'")
  (.then (.executeSql conn "select * from a where giglgc = 'ab'")
  #(js/console.log "result = " (.item (.-rows (aget % 0)) 0))       )
  )
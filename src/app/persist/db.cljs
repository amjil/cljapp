(ns app.persist.sqlite
  (:require
   [applied-science.js-interop :as j]
   [cljs-bean.core :as bean]
   [clojure.string :as str]
   [promesa.core :as p]
   ["react-native" :as rn]
   ["react-native-fs" :as fs]
   ["react-native-sqlite-storage" :as sqlite]))

;; (sqlite/enablePromise false)
(sqlite/enablePromise true)

(def conn (atom nil))

(defn open []
  (if-not @conn
    (let [platform    (j/get-in rn [:Platform :OS])
          file-prefix (condp = platform
                        "android" (str (as-> fs/DocumentDirectoryPath m
                                         (str/split m #"/")
                                         (drop-last m)
                                         (concat m ["databases"])
                                         (str/join "/" m)) "/")

                        "ios" (str fs/LibraryDirectoryPath "/LocalDatabase/"))]
      (.then
       (sqlite/openDatabase
        (bean/->js
         (merge
          {:name "cand.db"}

          (condp = platform
            "android"
            {:location "default"}
            "ios" {:createFromLocation (str file-prefix "cand.db")}))))
       #(reset! conn %)))))

(defn close []
  (when @conn
    (.close @conn)
    (reset! conn nil)))

(defn is-connected? []
  (if @conn true false))

(comment
  sqlite/openDatabase
  (open)
  (is-connected?)
  (close)
  conn
  @conn
  (.close @conn)
  (reset! conn nil)

  (.then (.executeSql @conn "select * from a where short_index = 'ab'")
  #(js/console.log "result = " (.item (.-rows (aget % 0)) 0))       )

  (p/let [result (.executeSql @conn "select * from a where short_index = 'ab'")]
    (p/then result 
      #(do (js/console.log ">>> " (.item (.-rows (aget % 0)) 0))   
           %)))
  
  
  )
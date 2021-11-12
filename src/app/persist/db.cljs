(ns app.persist.sqlite
  (:require
   [applied-science.js-interop :as j]
   [cljs-bean.core :as bean]
   [clojure.string :as str]
   [promesa.core :as p]
   [honey.sql :as hsql]
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

(defn rows-data [rows]
  (-> rows
    last
    (j/get :rows)
    (j/call :raw)))

(defn candidates [index-str return-fn]
  (cond
    (empty? index-str)
    []

    :else
    (let [table (first index-str)
          sql (hsql/format {:select [:id :full_index :short_index :char_word :active_order]
                            :from   [(keyword table)]
                            :where  [:or [:= :a.full_index index-str]
                                     [:= :a.short-index index-str]]})]
      (p/let [result (.executeSql @conn (first sql) (bean/->js (rest sql)))]
        (p/then result #(return-fn (rows-data %)))))))

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
         #(js/console.log "result = " (.item (.-rows (aget % 0)) 0)))

  (p/let [result (.executeSql @conn "select * from a where short_index = ?" (bean/->js ["ab"]))]
    (p/then result
            #(do (js/console.log ">>> " (.item (.-rows (aget % 0)) 0))
                 %)))

  (.then (.executeSql @conn "select * from a where short_index = ?" ["ab"])
         #(js/console.log "result = " ))
  (.then (.executeSql @conn (first asql) (bean/->js (rest asql)))
        ;;  #(js/console.log "result = " (.item (.-rows (aget % 0)) 0)))
         #(js/console.log "result = "  (bean/->js (.raw (rows-data %)))))

  
  (.then
   (.transaction @conn
                 (fn [tx]
                   (.executeSql tx (first asql) (bean/->js (rest asql))
                                (fn [tx res]
                                  (js/console.log "result = " res)))))
   #(js/console.log "yes >>>>" %))

  (candidates "ab" js/console.log)

  hsql/format
  asql
  (require '[honey.sql :as sql])
  (def asql
    (hsql/format {:select [:id :full_index :short_index]
                 :from   [:a]
                 :where  [:= :a.short_index "ab"]}))
  
  (range 2)
  (first "ab")
  )
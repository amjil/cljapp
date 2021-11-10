(ns app.persist.realm
  (:require
   [applied-science.js-interop :as j]
   [cljs-bean.core :as bean]
   [promesa.core :as p]
   [clojure.string :as str]
   ["react-native-fs" :as fs]))

; (def )
(def Realm (js/require "realm"))

(def suffix-schema {:name "phrase" :properties {:t1 "string" :id1 "int" :t2 "string" :id2 "int"}})
(def schema {:name "a" :properties {:id "int" :char_word "string" :full_index "string" :short_index "string" :active_order "int"}})
(def all-schema 
  (conj
   (map #(merge schema {:name %}) 
        ["a" "b" "c" "d" "e" "f" "g" "h" "i" "j" "k" "l" "m" "n" "o" "p" "q" "r" "s" "t" "u" "v" "w" "x" "y" "z"])))
   suffix-schema

(def realm (Realm. (clj->js {:schema all-schema})))

(defn import-data [alpha]
  (p/let [content (.readFileAssets fs "a.csv" "utf8")]
    (p/then content
            (fn [_]
              (doall
               (for [item (-> content
                              (str/replace #"\"" "")
                              (str/split  #"\r\n")
                              rest)]
                 (.write realm
                         (fn [] (.create realm "a"
                                         (clj->js
                                          (zipmap
                                           [:id :char_word :full_index :short_index :active_order]
                                           (let [[id word full short order] (str/split item #",")]
                                             [(js/parseInt id) (str/trim word) full short (js/parseInt order)]))))))))
              (js/console.log "import data ending ...")))))
(comment
  (import-data nil)
  (.-length (.objects realm "a"))
  (require '["realm" :as rea])
  rea/Realm
  (js/parseInt "123")
  (def dog {:name "Dog"
            :properties {:name "string?"
                         :age "int?"}})
  ;
  (def Realm (js/require "realm"))
  (def realm (Realm. (clj->js {:schema [dog] :schemaVersion 2})))

  (.write realm (fn []
                  (.create realm "Dog" (clj->js {:name "Rex" :age 3}))))
  ;
  (let [all-dogs (.objects realm "Dog")]
    (js/console.log "dogs >>>> " all-dogs))

  (Realm.copyBundledRealmFiles)
  ; Realm.defaultPath)
  fs/DocumentDirectoryPath
  ;;
  (.unlink fs (str fs/DocumentDirectoryPath "/default.realm"))
  (.unlink fs (str fs/DocumentDirectoryPath "/default.realm.lock"))
  (.unlink fs (str fs/DocumentDirectoryPath "/default.realm.management"))

  ;; (.copyFile fs )
  (.copyFileAssets fs "default.realm" (str fs/DocumentDirectoryPath "/default.realm"))
  (.then (.exists fs (str fs/DocumentDirectoryPath "/default.realm"))
         (fn [x] (js/console.log "exists >> " x)))

  (def a {:name "a" :properties {:id "int" :char_word "string" :full_index "string" :short_index "string" :active_order "int"}})
  a
  (def ss (map #(merge a {:name %}) ["a" "b" "c" "d" "e" "f" "g" "h" "i" "j" "k" "l" "m" "n" "o" "p" "q" "r" "s" "t" "u" "v" "w" "x" "y" "z"]))
  ss
  (def realm (Realm. (bean/->js {:schema ss :path (str fs/DocumentDirectoryPath "/default.realm")
                                 :schemaVersion 1})))

  (.then (.open Realm (bean/->js {:schema [a] :path (str fs/DocumentDirectoryPath "/default.realm")
                                  :schemaVersion 1}))
         (fn [x] (def realm x)))
  realm



  (count result)
  (count (str/split result #"\r\n"))
  (js/console.log "aaaa")
  [1 2 3]

  (.close realm)
  (let [all-a (.filtered (.objects realm "a") "short_index = 'ab'")]
    ;; (for [item all-a]
    ;;   (js/console.log "item >> " item))
    (js/console.log "dogs >>>> " (.-length all-a)))
  realm

  (p/let [result (.filtered (.objects realm "a") "short_index = 'ab'")]
    (p/then result (fn [_] (js/console.log result))))

  (p/let [items (j/call (j/call realm :objects "a") :filtered "short_index = 'ab'")]
    (p/then items #(do
                     (def it items)
                     (js/console.log "result >>>> 123"))))
  
  (.then (js/Promise.resolve items)
         #(do (js/console.log "result = >>> ")
              (def it %)))
  (type it)
  (.-length items)
  items
  (def item (first (bean/->clj items)))
  (js/console.log "items >>> length " (.-length items))
  (.keys js/Object items)
  (def item (aget items 0))
  (.keys js/Object item)
  (.-)
  (map #(empty? %) items)
  item
  (let [{:as data} (j/lookup item)]
    (js/console.log data))
  (bean/->clj item)
  (js/console.log "item >> " item)
  (js/console.log "items >> " items)
  (js/console.log "hello world")
  items
  (for [k (.keys js/Object item)]
    [k (aget item k)])
  (js/console.log (first (bean/->clj items)))
  (type items)
  (println items)

(def databaseOptions (bean/->js {:schema ss :path (str fs/DocumentDirectoryPath "/default.realm")
                                 :schemaVersion 1}))  

  (defn queryAll
    []
    (js/Promise.
      (fn [resolve reject]
        (->
          ((.-open Realm) {:schema [{:name "a" :properties {:id "int" :char_word "string" :full_index "string" :short_index "string" :active_order "int"}}] :path (str fs/DocumentDirectoryPath "/default.realm")
                                 :schemaVersion 1})
          (.then
            (fn [realm]
              (let [word-candidates (.filtered (.objects realm "a") "short_index = 'ab'")]
                (resolve word-candidates))))
          (.catch (fn [error] (reject error)))))))
  
  (def result (queryAll))
  (.then (queryAll) #(do (js/console.log "ssss")
  (def ite %)                   ))
  (.-length ite)
  (js/console.log "ite >>> " ite)
  (aget ite 0)
  (.keys js/Object (aget ite 0))
  )





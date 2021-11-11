(ns app.persist.fs
  (:require
   ["react-native-fs" :as fs]
   ["react-native" :as rn]
   [applied-science.js-interop :as j]
   [promesa.core :as p]))

(defn copy-file [filename]
  (let [platform (j/get-in rn [:Platform :OS])
        [copy-fn src-file]
        (condp = platform
          "android" [:copyFileAssets filename]
          "ios"     [:copyFile (str fs/MainBundlePath "/" filename)])
        des-file (str fs/DocumentDirectoryPath "/" "cand.db")]
    (.then (j/call fs copy-fn src-file des-file)
      (fn [x] (js/console.log "file copied !")))))

(defn check-file [filename]
  (.then (j/call fs :exists (str fs/DocumentDirectoryPath "/" filename))
    #(js/console.log filename " is " %)))

(comment
  fs/DocumentDirectoryPath
  fs/MainBundlePath
  ;; io fs/LibraryDirectoryPath 
  fs/LibraryDirectoryPath
  ;; an fs/ExternalDirectoryPath
  (prn "aa")
  (copy-file "database.db")
  (str fs/DocumentDirectoryPath "/" "cand.db")
  (str fs/DocumentDirectoryPath "/cand.db")
  (check-file "cand.db"))

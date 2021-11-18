(ns app.core
  (:require
   [steroid.rn.core :as rn]
   [app.font.base :as font]
   [app.ui.views :as views]
   [honey.sql :as hsql]
   ["react-native-sqlite-storage" :as sqlite]
   ["react-native-anchor-point" :refer [withAnchorPoint]]
   ["react-native-text-size" :as rnsize]
   app.events
   app.subs))


(defn init []
  (font/init)

  (rn/register-comp "cljapp" views/root-stack))

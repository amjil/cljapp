(ns app.core
  (:require
   [steroid.rn.core :as rn]
   [steroid.rn.navigation.safe-area :as safe-area]
   [steroid.rn.components.touchable :as touchable]
   [steroid.rn.components.ui :as rn-ui]
   [reagent.core :as reagent]
   ["react-native-svg" :as svg]
   [applied-science.js-interop :as j]
   [cljs-bean.core :refer [->clj ->js]]
   [goog.crypt.base64 :as b64]
   [app.font.base :as font]
   [app.text.index :as text]
   [clojure.string :as str]
   [app.components.gesture :as gesture]
   ["react-native-smooth-blink-view" :default blinkview]

   [app.ui.views :as views]
   [app.persist.realm :as realm]
   app.events
   app.subs))



(defn init []
  (font/init)
  (realm/Realm.copyBundledRealmFiles)


  (rn/register-comp "cljapp" views/root-stack))



(comment
  (require '[promesa.core :as p])
  @font/fonts
  (js/console.log "hahah"))

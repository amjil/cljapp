(ns app.ui.drawer.index
  (:require
   ["@react-navigation/drawer" :refer [createDrawerNavigator
                                       DrawerContentScrollView]]
   [reagent.core :as reagent]
   [app.ui.components :as ui]
   [cljs-bean.core :as bean]
   [applied-science.js-interop :as j]
   [steroid.rn.core :as rn]
   [steroid.rn.components.list :as rnlist]
   [steroid.rn.components.touchable :as touchable]
   [steroid.rn.navigation.core :as rnnav]
   [steroid.rn.utils :as utils]
   [promesa.core :as p]))

(defn create-drawer-navigator-screen []
  (let [^js drawer (createDrawerNavigator)]
    [(reagent/adapt-react-class (.-Navigator drawer))
     (reagent/adapt-react-class (.-Screen drawer))]))

(defn create-drawer-navigator []
  (let [^js drawer (createDrawerNavigator)]
    (reagent/adapt-react-class (.-Navigator drawer))))

(def drawer-content-scroll-view (reagent/adapt-react-class DrawerContentScrollView))

(defn drawer []
  (let [[navigator screen] (create-drawer-navigator-screen)]
    (utils/prepare-navigator navigator screen)))

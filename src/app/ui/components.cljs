(ns app.ui.components
  (:require
    [steroid.rn.core :as rn]
    [steroid.rn.components.platform :as platform]
    [steroid.rn.components.other :as other]
    ["react-native-vector-icons/Ionicons" :default ion-icons-class]
    ["react-native" :as react-native]
    [reagent.core :as reagent]
    [steroid.rn.navigation.safe-area :as safe-area]))

(def ion-icons (reagent/adapt-react-class ion-icons-class))
(def refresh-control-class (reagent/adapt-react-class react-native/RefreshControl))

(defn safe-area-consumer [& children]
  [safe-area/safe-area-consumer
   (fn [insets]
     (reagent/as-element
      (into [rn/view {:style {:flex             1 :padding-top (.-top insets)
                              :background-color :white}}]
            children)))])

(defn refresh-control [props]
  (reagent/as-element [refresh-control-class props]))

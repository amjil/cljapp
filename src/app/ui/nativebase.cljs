(ns app.ui.nativebase
  (:require
   [applied-science.js-interop :as j]
   [cljs-bean.core :as bean]
   [reagent.core :as reagent]
   [steroid.rn.core :as rn]
   [steroid.rn.navigation.core :as rnn]
   ["native-base" :refer [NativeBaseProvider
                          Center
                          Box]]))


(def nativebase-provider (reagent/adapt-react-class NativeBaseProvider))

(def box (reagent/adapt-react-class Box))

(def center (reagent/adapt-react-class Center))

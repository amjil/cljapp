(ns app.ui.nativebase
  (:require
   [applied-science.js-interop :as j]
   [cljs-bean.core :as bean]
   [reagent.core :as reagent]
   [steroid.rn.core :as rn]
   [steroid.rn.navigation.core :as rnn]
   [app.ui.text.index :as text]
   ["native-base" :refer [NativeBaseProvider
                          Center
                          Box
                          Button
                          useStyledSystemPropsResolver
                          useToast]]))

(def nativebase-provider (reagent/adapt-react-class NativeBaseProvider))

(def box (reagent/adapt-react-class Box))

(def button (reagent/adapt-react-class Button))

(def center (reagent/adapt-react-class Center))

(defn view2 []
  (let [text-props {:fontSize "md" :color :white}
        [props _] (useStyledSystemPropsResolver (bean/->js text-props))]
    [center {:flex 1 :px 3 :safeArea true}
     [box {:bg {:linearGradient {:colors ["lightBlue.300" "violet.800"]
                                 :start [0 0]
                                 :end [1 0]}}
           :p "12"
           :rounded "lg"
           :_text text-props
           :safeArea true}
      [text/text-view (merge {:text
                               "This is a Box with Linear Gradient"
                              :height "auto"}
                             (bean/->clj props))]]]))
                                   ; (bean/->clj (j/get props :_text)))]]]))

(defn view []
  (let [text-props {:fontSize "md" :color :white}
        [props _] (useStyledSystemPropsResolver (bean/->js text-props))
        toast (useToast)]
    (js/console.log "props = " (bean/->js props))
    [center {:flex 1 :px 3 :safeArea true}
     [button {:onPress (fn [] (j/call toast :show (bean/->js {:render (fn [] (reagent/as-element [box {:bg "emerald.500" :px "2" :py "1" :rounded "sm" :mb 5}
                                                                                                      "Hello! Have a nice day"]))})))}
       "Custom Toast"]]))

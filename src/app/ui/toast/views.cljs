(ns app.ui.toast.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as reagent]
    [app.text.index :as text]
    [applied-science.js-interop :as j]
    ["react-native" :as rnn]
    [cljs-bean.core :as bean]
    [steroid.rn.core :as rn]
    [steroid.rn.components.touchable :as touchable]
    [app.ui.components :as ui]
    [steroid.rn.components.ui :as rn-ui]))

(def animated (j/get rnn :Animated))

(def is-show (reagent/atom false))

(defn toast []
  (fn []
    [touchable/touchable-without-feedback {:on-press #(do (js/console.log "on-press >>>"))}
     [rn/view {:style {:position "absolute"
                       :left 0 :right 0
                       :top 0
                       :bottom 0
                       :elevation 999
                       :alignItems "center"
                       :justifyContent "center"
                       :z-index 10000}}
               ; :pointerEvents "auto"}
      ; [:> animated.View]
      [rn/view
        {:sytle {:backgroundColor "black"
                 :opacity 1
                 :borderRadius 5
                 :padding 10}}
       [rn/text {:style {:color "white"}} "Hello world!"]]]]))

(defn view []
  [ui/safe-area-consumer
   [rn/view {:style {:width "100%" :height "100%" :flex-direction "column"}}
    [rn-ui/button {:title "hello "}]
    [rn/view {}
     [toast]]]])
    ; [rn/view {:style {:width "100%" :height "100%"}}
(comment
  animated.View)

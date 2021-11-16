(ns app.ui.keyboard.candidates
  (:require
   [reagent.core :as reagent]
   [re-frame.core :refer [dispatch subscribe]]
   [app.text.index :as text]
   [applied-science.js-interop :as j]
   ["react-native" :as rnn]
   [cljs-bean.core :as bean]
   [steroid.rn.core :as rn]
   [steroid.rn.components.touchable :as touchable]
   [app.ui.components :as ui]
   [steroid.rn.components.list :as rn-list]
   [steroid.rn.components.ui :as rn-ui]))

(defn views []
  (let [candidates @(subscribe [:candidates-list])]
    [rn/view {:style {:position "absolute"
                      :left 0
                      :right 0
                    ;:top 0
                      :bottom 10
                      :elevation 1998
                      :alignItems "center"
                      :justifyContent "center"
                      :z-index 999}}
     [rn/view
      {:style {:opacity 0.6
               :backgroundColor "ghostwhite"
               :borderRadius 5
               :padding 10
               :height "auto"
               :alignItems "flex-start"
               :justifyContent "center"
               :maxWidth "50%"
               :minWidth 10
               :borderWidth 1
               :borderColor "lightgray"
               :flex-direction "row"}}
      [rn-list/flat-list
       {:key-fn    identity
        ;; :data []
        :data      (cond
                     (not-empty candidates)
                     candidates

                     :else
                     [])
        :render-fn (fn [x]
                     [touchable/touchable-opacity {:on-press #(dispatch [:candidate-select x])}
                      [rn/view {:style {}}
                       [text/text-inline {:width 30
                                          :fill "black"
                                          :font :white
                                          :font-size 18} (:char_word x)]]])
        :horizontal true}]]]))
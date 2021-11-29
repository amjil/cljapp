(ns app.ui.keyboard.candidates
  (:require
   [reagent.core :as reagent]
   [re-frame.core :refer [dispatch subscribe]]
   [applied-science.js-interop :as j]
   ["react-native" :as rnn]
   [cljs-bean.core :as bean]
   [steroid.rn.core :as rn]
   [steroid.rn.components.touchable :as touchable]
   [app.ui.components :as ui]
   [app.ui.text.index :as text]
   [steroid.rn.components.list :as rn-list]
   [steroid.rn.components.ui :as rn-ui]))

(defn views []
  (let [candidates @(subscribe [:candidates-list])]
    (cond
      (empty? candidates)
      nil

      :else
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
                 ; :height "auto"
                 ; :maxheight 100
                 :min-height 60
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
                        [rn/view {:style {:height "100%"}}
                         [text/text-view {:font-family "MongolianWhite" :text (:char_word x) :font-size 12}]]])
          :initialNumToRender 7
          :showsHorizontalScrollIndicator false
          :horizontal true}]]])))

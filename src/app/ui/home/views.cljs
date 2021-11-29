(ns app.ui.home.views
  (:require
   [reagent.core :as reagent]
   [app.ui.components :as ui]
   [app.font.base :as font]
   [cljs-bean.core :as bean]
   [applied-science.js-interop :as j]
   [steroid.rn.core :as rn]
   [steroid.rn.components.list :as rnlist]
   [steroid.rn.components.touchable :as touchable]
   [promesa.core :as p]
   [re-frame.core :refer [dispatch subscribe]]
   [app.ui.text.index :as text]))


(defn home []
  (let [h (reagent/atom nil)
        height 300
        width 20
        offset (- (/ height 2) (/ width 2))]
    (fn []
      [ui/safe-area-consumer
      ;; OK this style
      ;;  [rn/view {:style {}}
      ;;        [rn/text {:style {:width height :height width
      ;;                                :color "black"
      ;;                                :backgroundColor "yellow"
      ;;                                :transform [{:rotate "90deg"}
      ;;                                            {:translateX offset}
      ;;                                            {:translateY offset}]}}
      ;;                 ;;  :selectable true

      ;;          "hello world!"]]]

      ;;  [rn/view {:style {:width  width :height height :backgroundColor "red"}}
      ;;        [rn/text {:style {:width height :height width
      ;;                          :backgroundColor "yellow"
      ;;                          :transform [{:rotate "90deg"}
      ;;                                      {:translateX offset}
      ;;                                      {:translateY offset}
      ;;                                      ]}
      ;;                 ;;  :selectable true
      ;;                  }
      ;;          "hello world!" ]
      ;;   ]
      ;;  [rn/view {:style {:width "100%"
      ;;                    :flex 2}
      ;;            :on-layout #(let [height (j/get-in % [:nativeEvent :layout :height])]
      ;;                          (reset! h height))}
      ;;   (when @h
      ;;     (let [offset (- (/ @h 2) (/ width 2))]
      ;;       [rn/view {:style {:height @h :width width :backgroundColor "red"}}
      ;;        [rn/text {:style {:width @h :height width
      ;;                          :backgroundColor "yellow"
      ;;                          :transform [{:rotate "90deg"}
      ;;                                      {:translateX offset}
      ;;                                      {:translateY offset}]}}
      ;;          "hello world!"]
      ;;        ]))]
       ; [text-view {:text (apply str (repeat 20 "But it does have drawbacks: this approach will block the thread until all of the chained callbacks are executed. For small chains this is not a problem. However, if your chain has a lot of functions and requires a lot of computation time, this might cause unexpected latency. It may block other threads in the thread pool from doing other, maybe more important, tasks."))}])))
       [rn/text "hello world!"]])))
       ; [text/text-view {
       ;                  ; :fontFamily "NotoSansMongolian-Regular"
       ;                  ; :fontFamily "MongolianWhite"
       ;                  ; :fontFamily "Menk Qagan Tig"
       ;                  :fontSize 18
       ;                  :text font/mlongstr}]])))





(comment

  (require '[app.font.base :as font])
  (require '["react-native-measure-text-chars" :as rnmeasure])
  rnmeasure
  (rand-int 10000)
  (gensym "key-")

  (.then
   (rnmeasure/measure (bean/->js {:text "hello world!"}))
   #(js/console.log "aaa " (j/get % "lineInfo")))

  (p/then
   (rnmeasure/measure (bean/->js {:text "hello world!"}))
   #(js/console.log "aaa " (def aa (bean/->clj %))))
  aa

  ;; (p/let [result (j/call rnsize :measure (bean/->js {:text "hello world!"}))]

  (prn 'Hi))

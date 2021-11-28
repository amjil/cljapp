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
   ["react-native-measure-text-chars" :as rntext]))

(defn text-view [props]
  (let [info (reagent/atom nil)
        h (reagent/atom nil)
        flat-data (reagent/atom nil)]

    (fn []
      [rn/view {:style {;:width "100%"
                        :flex-direction "row"
                        :flex 1}

                :on-layout #(let [height (j/get-in % [:nativeEvent :layout :height])]

                              (reset! h height)

                              (p/then
                               (rntext/measure (bean/->js (assoc props :width height)))
                               (fn [result]
                                 (let [data (bean/->clj result)
                                       text (:text props)]
                                   (reset! flat-data (map (fn [x] (subs text (:start x) (:end x))) (:lineInfo data)))
                                   (reset! info data)))))}


       (when (and @h @info)
         (let [line-width (max (:line-width props) (/ (:height @info) (:lineCount @info)))
               offset (- (/ @h 2) (/ line-width 2))]
           [rnlist/flat-list
            {:key-fn    (fn [_ index] (str "text-" index))
             :data    @flat-data
             :render-fn
             (fn [x]
               [touchable/touchable-without-feedback {}
                 [rn/view {:style {:height @h :width line-width :backgroundColor "red"}}
                   [rn/text {:style {:width @h :height line-width
                                     :backgroundColor "yellow"
                                     ; :fontFamily "NotoSansMongolian-Regular"
                                     ; :fontFamily "MongolianWhite"
                                     ; :fontFamily "Menk Qagan Tig"
                                     :fontSize 18
                                     :transform [{:rotate "90deg"}
                                                 {:translateX offset}
                                                 {:translateY offset}]}}
                    x]]])
             :horizontal true
             :removeClippedSubviews true
             :initialNumToRender 20}]))])))

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
       [text-view {
                   ; :fontFamily "NotoSansMongolian-Regular"
                   ; :fontFamily "MongolianWhite"
                   ; :fontFamily "Menk Qagan Tig"
                   :fontSize 18
                   :text font/mlongstr}]])))





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

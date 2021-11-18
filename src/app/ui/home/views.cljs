(ns app.ui.home.views
  (:require
   [reagent.core :as reagent]
   [app.ui.components :as ui]
   [cljs-bean.core :as bean]
   [applied-science.js-interop :as j]
   [steroid.rn.core :as rn]
   [promesa.core :as p]
   ["react-native-anchor-point" :refer [withAnchorPoint]]))


(defn home []
  (let [h (reagent/atom nil)
        height 20
        width 300
        offset (- (/ height 2) (/ width 2))]
    (fn []
      [ui/safe-area-consumer
      ;;  [rn/view {:style {:width "100%" :height "100%" :alignItems "flex-start" :justifyContent "flex-start"}}
      ;; has some transform issue
      ;;  [rn/view {:style {:width height :height width :backgroundColor "red"}}
      ;;   [rn/text {:style (merge (bean/->clj (withAnchorPoint (bean/->js {:transform [{:perspective 400}
      ;;                                                                         {:rotate "90deg"}]})
      ;;                                                 (bean/->js {:x -0 :y 1})
      ;;                                                 (bean/->js {:width width, :height height})))
      ;;                           {:height height :width width :backgroundColor "yellow"}
      ;;   )}
      ;;    "hello world!"]
      ;;  ]
      ;; OK this style
       [rn/view {:style {:width  height :height width :backgroundColor "red"}}
             [rn/text {:style {:width width :height height
                               :backgroundColor "yellow"
                               :transform [{:rotate "90deg"}
                                           {:translateX (- offset)}
                                           {:translateY (- offset)}
                                           ]}}
               "hello world!" ]
        ]
      ;;  [rn/view {:style {:width "100%"
      ;;                    :flex 2}
      ;;            :on-layout #(let [height (j/get-in % [:nativeEvent :layout :height])]
      ;;                          (reset! h height))}
      ;;   (when @h
      ;;     (let [offset (- (/ @h 2) (/ 20 2))]
      ;;       [rn/view {:style {:height @h :width 20}}
      ;;        [rn/text {:style {:width @h :height 20 :transform [{:rotate "90deg"}
      ;;                                                           {:translateX (- offset)}
      ;;                                                           {:translateY offset}]}}
      ;;          "abc"]
      ;;        ]))]
       ])))
         


(comment
  (require '["react-native-anchor-point" :refer [withAnchorPoint]])
  withAnchorPoint

  (bean/->clj
   (withAnchorPoint (bean/->js {:transform [{:perspective 400}
                                            {:rotate 90}]})
                    (bean/->js {:x 0.5 :y 0})
                    (bean/->js {:width 20, :height 80})))
  (require '[app.font.base :as font])
  (require '["react-native-text-size" :default rnsize])

  ;; (p/let [result (j/call rnsize :measure (bean/->js {:text "hello world!"}))]
  (.then (j/call rnsize :measure (bean/->js {:text "hello world!"})) #(js/console.log "aaa " %))
  (.then (j/call rnsize :measure (bean/->js {:text (apply str (repeat 100 font/mlongstr))})) #(js/console.log "bbb " %))
  (j/call rnsize :measure (bean/->js {:text "hello world!"}) #(js/console.log "aaa " %))

  rnsize
  (rnsize/measure
   {:text
    "hello world!"})


  (prn 'Hi)
  )
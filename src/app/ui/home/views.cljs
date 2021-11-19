(ns app.ui.home.views
  (:require
   [reagent.core :as reagent]
   [app.ui.components :as ui]
   [cljs-bean.core :as bean]
   [applied-science.js-interop :as j]
   [steroid.rn.core :as rn]
   [promesa.core :as p]))


(defn home []
  (let [h (reagent/atom nil)
        height 300
        width 20
        offset (- (/ height 2) (/ width 2))]
    (fn []
      [ui/safe-area-consumer
      ;; OK this style
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
       [rn/view {:style {:width "100%"
                         :flex 2}
                 :on-layout #(let [height (j/get-in % [:nativeEvent :layout :height])]
                               (reset! h height))}
        (when @h
          (let [offset (- (/ @h 2) (/ 20 2))]
            [rn/view {:style {:height @h :width width :backgroundColor "red"}}
             [rn/text {:style {:width @h :height width
                               :backgroundColor "yellow"
                               :transform [{:rotate "90deg"}
                                           {:translateX offset}
                                           {:translateY offset}]}}
               "hello world!"]
             ]))]
       ])))
         


(comment

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
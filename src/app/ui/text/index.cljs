(ns app.ui.text.index
  (:require
    [cljs-bean.core :as bean]
    [applied-science.js-interop :as j]
    [app.ui.components :as ui]
    [steroid.rn.core :as rn]
    [steroid.rn.components.list :as rnlist]
    [steroid.rn.components.touchable :as touchable]
    [promesa.core :as p]
    [re-frame.core :refer [dispatch subscribe]]
    [reagent.core :as reagent]
    ["react-native-measure-text-chars" :as rntext]))

(defn single-line [props text]
  [touchable/touchable-without-feedback {}
   [rn/view {:style {:height (:height props) :width (:line-width props)}} ;:backgroundColor "red"}}
    [rn/text {:style (merge
                       (dissoc props :data :text :offset :line-width)
                       {:width (:height props) :height (:line-width props)
                        :transform [{:rotate "90deg"}
                                    {:translateX (:offset props)}
                                    {:translateY (:offset props)}]})}
     (:text props)]]])

(defn multi-line [props]
  [rnlist/flat-list
   {:key-fn    (fn [_ index] (str "text-" index))
    :data    (:data props)
    :horizontal true
    :removeClippedSubviews true
    :initialNumToRender 20
    :render-fn
    (fn [x]
      [touchable/touchable-without-feedback {}
        [rn/view {:style {:height (:height props) :width (:line-width props)}}; :backgroundColor "red"}}
          [rn/text {:style (merge
                             (dissoc props :data :text :offset :line-width)
                             {:width (:height props) :height (:line-width props)
                              ; :backgroundColor "yellow"
                              ; :fontSize 18
                              :transform [{:rotate "90deg"}
                                          {:translateX (:offset props)}
                                          {:translateY (:offset props)}]})}
           x]]])}])

(defn text-view [props]
  (let [info (reagent/atom nil)
        h (reagent/atom 0)
        flat-data (reagent/atom nil)]

    (fn []
      [rn/view {:style {:height (cond
                                  (= "auto" (:height props))
                                  (if @h @h 0)

                                  :else
                                  "100%")}


                :on-layout #(let [height (j/get-in % [:nativeEvent :layout :height])]

                              ; (reset! h height)

                              (if-not @info
                                (let [props (if (= "auto" (:height props))
                                              props
                                              (assoc props :width height))]
                                  (p/then
                                   (rntext/measure (bean/->js props))
                                   (fn [result]
                                     (let [data (bean/->clj result)
                                           text (:text props)]
                                       (reset! flat-data (map (fn [x] (subs text (:start x) (:end x))) (:lineInfo data)))
                                       (reset! info data)
                                       (reset! h (if (= "auto" (:height props))
                                                   (:width data)
                                                   height))))))))}

       (when (and @h @info)
         (let [line-width (max (:line-width props) (/ (:height @info) (:lineCount @info)))
               offset (- (/ @h 2) (/ line-width 2))
               props (assoc props :height @h :line-width line-width :offset offset :data @flat-data)]
           (if (= 1 (:lineCount @info))
             [single-line props]
             [multi-line props])))])))

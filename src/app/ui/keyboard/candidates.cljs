(ns app.ui.keyboard.candidates
  (:require
   [reagent.core :as reagent]
   [re-frame.core :refer [dispatch subscribe]]
   [applied-science.js-interop :as j]
   [cljs-bean.core :as bean]
   [app.ui.nativebase :as nbase]
   [app.ui.components :as ui]
   [app.ui.keyboard.bridge :as bridge]
   [app.persist.sqlite :as sqlite]
   [clojure.string :as str]))

(def candidates-list (reagent/atom []))
(def candidates-index (reagent/atom ""))


(defn candidate-select [m]
  (reset! candidates-index "")
  (sqlite/next-words m
    #(reset! candidates-list %))
  (bridge/editor-insert (:char_word m)))


(defn candidates-query [i]
  (let [ii (str @candidates-index i)]
    (reset! candidates-index ii)
    (sqlite/candidates
      ii
      #(reset! candidates-list %))))

(defn candidates-delete []
  (let [old-index @candidates-index
        new-index (str/join "" (drop-last old-index))]
    (cond
      (or (empty? old-index) (= 1 (count old-index)))
      (if (empty? @candidates-list)
        (do
          (reset! candidates-index "")
          (reset! candidates-list [])
          (bridge/editor-delete))

        (do
          (reset! candidates-index "")
          (reset! candidates-list [])))

      :else
      (do
        (reset! candidates-index new-index)
        (sqlite/candidates
          new-index
          #(reset! candidates-list %))))))

(defn views []
  (fn []
    (let [candidates @candidates-list]
      (cond
        (empty? candidates)
        nil

        :else
        [nbase/box {:style {:position "absolute"
                            :left 0
                            :right 0
                            ;:top 0
                            :bottom 300
                            :elevation 1998
                            :alignItems "center"
                            :justifyContent "center"
                            :z-index 999}}
         [nbase/box
          {:style {;:opacity 0.6
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
          [nbase/flat-list
           {:keyExtractor    (fn [_ index] (str "text-" index))
            :data      (cond
                         (not-empty candidates)
                         candidates

                         :elsex
                         [])
            :renderItem (fn [x]
                          (let [{:keys [item index separators]} (j/lookup x)]
                            (reagent/as-element
                              [nbase/pressable {:on-press #(candidate-select (bean/->clj item))}
                               [nbase/box {:style {:height "100%" :width 28}}
                                [nbase/measured-text {:fontFamily "MongolianBaiZheng" :fontSize 18}
                                  (j/get item :char_word)]]])))
            :initialNumToRender 7
            :showsHorizontalScrollIndicator false
            :horizontal true}]]]))))

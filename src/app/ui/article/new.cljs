(ns app.ui.article.new
  (:require
    [app.ui.nativebase :as nbase]
    [app.ui.editor :as editor]
    [app.ui.components :as ui]
    [app.ui.text :as text]
    [app.ui.editor :refer [weblen]]
    [app.ui.keyboard.index :as keyboard]
    [app.ui.keyboard.candidates :as candidates]
    [app.ui.keyboard.bridge :as bridge]
    [app.text.message :refer [labels]]

    [steroid.rn.core :as rn]
    [applied-science.js-interop :as j]
    [cljs-bean.core :as bean]
    [reagent.core :as reagent]
    [re-frame.core :as re-frame]

    ["react-native-vector-icons/Ionicons" :default Ionicons]))

(def model (reagent/atom {:title "" :content ""}))

(def recommandations [{:title "hello world" :focus_count "100" :answer_count "991"}
                      {:title "The big big world" :focus_count "10" :answer_count "89"}
                      {:title "The Center of Asia" :focus_count "1" :answer_count "9"}])

(defn recomm-view [h]
  [nbase/box {:height (str (- @h 220) "px") :bg "light.200"
              :maxWidth 32}
   ; [nbase/box {:p 5}
    ;
    [nbase/flat-list
     {:keyExtractor    (fn [_ index] (str "question-view-" index))
      :data      recommandations

      :showsHorizontalScrollIndicator false
      :horizontal true
      :style {:flex 1}

      :renderItem (fn [x]
                    (let [{:keys [item index separators]} (j/lookup x)]
                      (reagent/as-element
                       [nbase/box {:flex 1 :flexDirection "row"}
                        [text/measured-text {:fontSize 18 :width (- @h 220 20)} (j/get item :title)]])))}]])
                        ; [nbase/box
                        ;  [text/measured-text {:fontSize 18} (j/get item :focus_count)]
                        ;  [text/measured-text {:fontSize 19} "-"]
                        ;  [text/measured-text {:fontSize 18} (j/get item :answer_count)]]])))}]])
                        ;


(defn view []
  (let [on-edit (reagent/atom false)
        h (reagent/atom nil)
        focus-elem (reagent/atom 0)

        recomm-flag (reagent/atom false)]
    (fn []
      [nbase/box
       {:on-layout #(let [height (j/get-in % [:nativeEvent :layout :height])]
                      (reset! h height))
        :flex 1}
       [nbase/zstack {:flex 1}
        (if-not (nil? @h)
          [nbase/flex {:flex 1 :flex-direction "row" :bg "white"}
           (if (= 1 @focus-elem)
             [nbase/zstack {:style {:height (- @h 220)}
                            :minWidth 10}
              [editor/editor-view
               ; opts
               {;:placeholder (get-in labels [:question :title-placeholder])
                :type :text}
               ;content-fn
               (fn []
                 ; (js/console.log "content -fn >......."))
                 (:title @model))
                 ; (get @model @active-key))
               ;update-fn
               (fn [x] (js/console.log "1112")
                 (swap! model assoc :title (:text x))
                 (reset! recomm-flag true))]
              (if (= 0 @weblen)
                [nbase/box {:flex 1 :m 5}
                 [text/multi-line-text {:fontSize 22 :color "#71717a" :fontFamily "MongolianBaiZheng" :width (- @h 220)}
                  (get-in labels [:question :title-placeholder])]])]
             [rn/touchable-opacity {:style {:paddingVertical 20
                                            :paddingLeft 20
                                            :height (- @h 220)}
                                    :onPress (fn [] (js/console.log "touchable without feedback .....")
                                               (bridge/editor-content)
                                               (reset! focus-elem 1)
                                               (reset! weblen (count (:title @model)))
                                               (candidates/candidates-clear))}
              [text/measured-text {:fontSize 22 :color "#71717a" :fontFamily "MongolianBaiZheng" :width (- @h 220)}
               (if (empty? (:title @model))
                 (get-in labels [:question :title-placeholder])
                 (:title @model))]])
           [nbase/box {:my 5}
            [nbase/divider (merge {:orientation "vertical" :mr "3" :ml "1"}
                             (if (= 1 @focus-elem)
                               {:thickness "2" :bg "blue.700"}))]]
           (if (= 2 @focus-elem)
             [nbase/zstack {:style {:height (- @h 220)}
                            :minWidth 20}
              [editor/editor-view
               ; opts
               {:quill {:placeholder (get-in labels [:question :content-placeholder])}
                :type :text}
               ;content-fn
               (fn []
                 ; (js/console.log "content -fn >......."))
                 (:content @model))
                 ; (get @model @active-key))
               ;update-fn
               (fn [x] (js/console.log "1112")
                 (swap! model assoc :content (:text x)))]
              (if (= 0 @weblen)
                [nbase/box {:flex 1 :m 5}
                 [text/measured-text {:fontSize 18 :color "#71717a" :fontFamily "MongolianBaiZheng" :width (- @h 220)}
                  (get-in labels [:question :content-placeholder])]])]
             [rn/touchable-opacity {:style {:paddingVertical 20
                                            :height (- @h 220)}
                                    :onPress (fn [] (js/console.log "touchable without feedback .....2")
                                               (bridge/editor-content)
                                               (reset! focus-elem 2)
                                               (reset! weblen (count (:content @model)))
                                               (candidates/candidates-clear))}
              [text/measured-text {:fontSize 18 :color "#71717a" :fontFamily "MongolianBaiZheng" :width (- @h 220)}
               (if (empty? (:content @model))
                 (get-in labels [:question :content-placeholder])
                 (:content @model))]])
           [rn/touchable-opacity {:on-press #(reset! recomm-flag (not @recomm-flag))}
            [text/measured-text {:font-size 18} "button"]]])
        [nbase/presence-transition {:visible @recomm-flag :initial {:opacity 0}
                                                       :animate {:opacity 1 :transition {:duration 250}}
                                    :position "absolute" :left 60}
         [recomm-view h]]
        [candidates/views {:bottom 20}]]
       [nbase/box {:height 220 :mt 1}
        [keyboard/keyboard {:type "single-line"
                            :on-press (fn []
                                        (bridge/editor-content)
                                        (candidates/candidates-clear))}]]])))

(def model-new
  {:name       :model-new
   :component  view

   :options
   {:title ""
    :headerRight
    (fn [tag id classname]
      (reagent/as-element
        [nbase/icon-button {:variant "ghost" :colorScheme "indigo"
                            :icon (reagent/as-element [nbase/icon {:as Ionicons :name "ios-checkmark"}])
                            :on-press (fn [e] (js/console.log "on press icon button"))}]))}})
                                        ; (bridge/editor-content)
                                        ; (re-frame/dispatch [:navigate-back]))}]))}})

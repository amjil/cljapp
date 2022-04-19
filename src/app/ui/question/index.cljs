(ns app.ui.question.index
  (:require
    [app.ui.nativebase :as nbase]
    [app.ui.editor :as editor]
    [app.ui.components :as ui]
    [app.ui.text :as text]
    [app.ui.keyboard.index :as keyboard]
    [app.ui.keyboard.candidates :as candidates]
    [app.ui.keyboard.bridge :as bridge]
    [app.handler.gesture :as gesture]

    [steroid.rn.core :as rn]
    [applied-science.js-interop :as j]
    [cljs-bean.core :as bean]
    [reagent.core :as reagent]
    [re-frame.core :as re-frame]

    ["react-native-vector-icons/Ionicons" :default Ionicons]))

(def questions-atom (reagent/atom [{:question_content "ᠲᠡᠷᠭᠡᠯ ᠰᠠᠷᠠ ᠭᠡᠵᠦ ᠶᠠᠮᠠᠷ ᠰᠠᠷᠠ ᠪᠤᠢ?"
                                    :question_detail "ᠲᠡᠷᠭᠡᠯ ᠰᠠᠷᠠ᠂ ᠬᠠᠪᠢᠷᠭᠠᠨ ᠰᠠᠷᠠ᠂ ᠲᠠᠯ᠎ᠠ ᠰᠠᠷᠠ\nabc\naaa\n111\n2222\n2222"
                                    :user_name "ᠪᠠᠲᠦ"
                                    :agree_count 0
                                    :comment_count 0}
                                   {:question_content "ᠬᠦᠬᠡ ᠲᠣᠯᠪᠤ ᠭᠡᠰᠡᠨ ᠨᠢ ᠶᠠᠭᠤ ᠪᠤᠢ?"
                                    :question_detail "ᠬᠦᠬᠡ ᠲᠤᠯᠪᠤᠲᠠᠨ ᠃"
                                    :user_name "ᠰᠣᠳᠤ"
                                    :agree_count 0
                                    :comment_count 0}
                                   {:question_content "ᠪᠢᠳᠡ ᠬᠡᠨ ᠪᠤᠢ"
                                    :question_detail ""
                                    :user_name "ᠪᠠᠭᠠᠲᠤᠷ"
                                    :agree_count 0
                                    :comment_count 0}
                                   {:question_content "ᠶᠠᠭᠠᠬᠢᠪᠠᠯ ᠵᠣᠬᠢᠬᠤ ᠪᠤᠢ"
                                    :question_detail "ᠶᠠᠭᠠᠬᠢᠵᠤ ᠠᠵᠢᠯᠯᠠᠪᠠᠯ ᠰᠠᠶᠢ ᠦᠷᠢ ᠪᠦᠲᠦᠮᠵᠢ ᠪᠡᠷ ᠰᠠᠢᠨ ᠪᠤᠢ"
                                    :user_name "ᠰᠦᠬᠡ"
                                    :agree_count 0
                                    :comment_count 0}
                                   {:question_content "ᠪᠢ ᠬᠠᠮᠢᠭ᠎ᠠ ᠲᠦᠷᠦᠪᠡ?"
                                    :question_detail "ᠪᠢᠳᠡᠨ "
                                    :user_name "ᠰᠠᠷᠠ"
                                    :agree_count 0
                                    :comment_count 0}
                                   {:question_content "ᠮᠠᠰᠢᠨ ᠭᠡᠳᠡᠭ ᠭᠠᠳᠠᠭᠠᠳᠤ ᠦᠭᠡ ?"
                                    :question_detail "ᠮᠠᠰᠢᠨ ᠭᠡᠵᠦ "
                                    :user_name "ᠰᠠᠷᠠ"
                                    :agree_count 0
                                    :comment_count 0}]))

(def cursor (reagent/atom 0))

(def model (reagent/atom {}))
(def active-key (reagent/atom nil))

(defn list-view []
  (let [height (reagent/atom nil)]
    (fn []
     [nbase/zstack {:flex 1
                    ; :bg "gray.100"
                    :bg "white"
                    :on-layout #(let [height (j/get-in % [:nativeEvent :layout :height])]
                                  (js/console.log "height = " height))}
      (if @height
        [nbase/hstack {:style {:height 674}}
         [nbase/box {:m 1 :p 4
                     :borderRightWidth "0.5"
                     :borderColor "gray.300"
                     :bg "white"}
          [nbase/skeleton {:h "100%" :w 24}]]]
        [nbase/flat-list
         {:keyExtractor    (fn [_ index] (str "question-view-" index))
          :data      @questions-atom
          ; :onRefresh (fn [e] (js/console.log "on-refreshing ... "))
          ; :refreshing false
          :renderItem (fn [x]
                        (let [{:keys [item index separators]} (j/lookup x)]
                          (reagent/as-element
                            [nbase/pressable {:m 1
                                              :p 4
                                              :borderRightWidth "0.5"
                                              :borderColor "gray.300"
                                              :bg "white"
                                              :on-press (fn [e] (re-frame/dispatch [:navigate-to :question-detail])
                                                          (reset! cursor index)
                                                          (reset! model (bean/->clj item)))}
                             [nbase/hstack
                              [nbase/vstack
                               [nbase/box {:bg "gray.300"
                                           :borderRadius "md"
                                           :p 4
                                           :alignSelf "center"}]
                               [nbase/box {:alignSelf "center"
                                           :mt 2
                                           :pl 2}
                                [text/measured-text {:fontFamily "MongolianBaiZheng" :fontSize 18 :color "#71717a"}
                                  (j/get item :user_name)]]]
                              [nbase/box {:mt 9
                                          :ml 2}
                               [text/measured-text {:fontFamily "MongolianBaiZheng" :fontSize 22 :color "#002851"}
                                 (j/get item :question_content)]]
                              [nbase/box {:mt 9
                                          :ml 1
                                          ; :w 100
                                          :flex 1}
                               [text/simple-text {:fontFamily "MongolianBaiZheng" :fontSize 18 :color "#71717a"}
                                 (j/get item :question_detail)]]
                              [nbase/box {:ml 2
                                          :mt 9
                                          :flex 1}
                               [nbase/box
                                [nbase/icon {:as Ionicons :name "ios-ellipsis-vertical-sharp"
                                             :size "6" :color "indigo.500"
                                             :mb 40}]]
                               [nbase/box {:justifyContent "flex-start"}
                                [nbase/icon {:as Ionicons :name "ios-heart-sharp"
                                             :size "6" :color "indigo.500"
                                             :mb 6}]
                                [nbase/icon {:as Ionicons :name "ios-chatbubble-ellipses-outline"
                                             :size "6" :color "indigo.500"
                                             :mb 6}]
                                [nbase/icon {:as Ionicons :name "time-outline"
                                             :size "6" :color "gray.300"
                                             :mb 1}]
                                [nbase/box {:alignSelf "center"}
                                 [text/measured-text {:color "gray.300"} "2022-04-10 13:52:54"]]]]]])))
          :showsHorizontalScrollIndicator false
          :horizontal true
          :flex 1
          :h "100%"}])
      [nbase/box {:right 4
                  :bottom 2}
                  ; :position "absolute"}
       [nbase/icon-button {:w 16 :h 16 :borderRadius "full" :variant "solid" :colorScheme "indigo"
                           :justifyContent "center" :alignSelf "center" :alignItems "center"
                           :icon (reagent/as-element [nbase/icon {:as Ionicons :name "ios-add"}])
                           :onPress (fn [e]
                                      (js/console.log "icon-button on press")
                                      (re-frame/dispatch [:navigate-to :question-detail]))}]]])))


(defn detail-view []
  [ui/safe-area-consumer
   [nbase/flex {:flex 1
                :p 5
                :flex-direction "row"
                :bg "white"}
    [nbase/box {:p 2}
     [text/measured-text {:fontSize 18 :fontFamily "MongolianBaiZheng"} "ᠠᠰᠠᠭᠤᠯᠲᠠ"]]
    [rn/touchable-highlight {:style {:borderWidth 1 :borderColor "#06b6d4"
                                     :paddingHorizontal 8
                                     :paddingVertical 20
                                     :borderRadius 8}
                              :underlayColor "#cccccc"
                              :on-press (fn [] (reset! active-key :question_content)
                                          (re-frame/dispatch [:navigate-to :question-edit]))}
     [text/measured-text {:fontSize 18 :fontFamily "MongolianBaiZheng"} (:question_content @model)]]
    [nbase/box {:p 2 :ml 2}
     [text/measured-text {:fontSize 18 :fontFamily "MongolianBaiZheng"} "ᠲᠠᠢᠯᠪᠤᠷᠢ"]]
    [gesture/tap-gesture-handler
     { :style {:flex 1}
       :onHandlerStateChange #(let [state (j/get-in % [:nativeEvent :state])]
                                (when (gesture/tap-state-end (j/get % :nativeEvent))
                                  (reset! active-key :question_detail)
                                  (re-frame/dispatch [:navigate-to :question-edit])))}
     [nbase/box {:style {:flex 1
                         :borderWidth 1 :borderColor "#06b6d4"
                         :paddingHorizontal 8
                         :paddingVertical 20
                         :borderRadius 8}}
      [text/measured-text {:fontSize 18 :fontFamily "MongolianBaiZheng"} (:question_detail @model)]]]]])



(defn edit-view []
  [ui/safe-area-consumer
   [nbase/flex {:flex 1
                :justifyContent "space-between"}
    [editor/editor-view
      {:type :text}
      ;content-fn
      (fn []
        ; (js/console.log "content -fn >......." (:content @model))
        (get @model @active-key))
      ;update-fn
      (fn [x]
        (swap! model assoc @active-key (get x :text))
        (swap! questions-atom assoc-in [@cursor @active-key] (get x :text)))]
    [candidates/views]
    [nbase/box {:height 220}
     [keyboard/keyboard]]]])

(def question-detail
  {:name       :question-detail
   :component  detail-view
   :options
   {:title ""}})

(def question-edit
  {:name       :question-edit
   :component  edit-view
   :options
   {:title ""
    :headerRight
    (fn [tag id classname]
      (reagent/as-element
        [nbase/icon-button {:variant "ghost" :colorScheme "indigo"
                            :icon (reagent/as-element [nbase/icon {:as Ionicons :name "ios-checkmark"}])
                            :on-press (fn [e] (js/console.log "on press icon button")
                                        (bridge/editor-content)
                                        (re-frame/dispatch [:navigate-back]))}]))}})

(def question-list
  {:name       :question-list
   :component  list-view
   :options
   {:title ""}})

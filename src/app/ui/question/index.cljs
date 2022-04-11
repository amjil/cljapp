(ns app.ui.question.index
  (:require
    [app.ui.nativebase :as nbase]
    [app.ui.editor :as editor]
    [app.ui.components :as ui]
    [app.ui.text :as text]
    [app.ui.keyboard.index :as keyboard]
    [app.ui.keyboard.candidates :as candidates]
    [app.ui.keyboard.bridge :as bridge]

    [applied-science.js-interop :as j]
    [cljs-bean.core :as bean]
    [reagent.core :as reagent]
    [re-frame.core :as re-frame]

    ["react-native-vector-icons/Ionicons" :default Ionicons]))

(def questions-atom (reagent/atom [{:question_content "ᠲᠡᠷᠭᠡᠯ ᠰᠠᠷᠠ ᠭᠡᠵᠦ ᠶᠠᠮᠠᠷ ᠰᠠᠷᠠ ᠪᠤᠢ?"
                                    :question_detail "ᠲᠡᠷᠭᠡᠯ ᠰᠠᠷᠠ᠂ ᠬᠠᠪᠢᠷᠭᠠᠨ ᠰᠠᠷᠠ᠂ ᠲᠠᠯ᠎ᠠ ᠰᠠᠷᠠ"
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

(defn list-view []
  [nbase/zstack {:flex 1
                 :bg "gray.100"}
   [nbase/flat-list
    {:keyExtractor    (fn [_ index] (str "question-view-" index))
     :data      @questions-atom
     :renderItem (fn [x]
                   (let [{:keys [item index separators]} (j/lookup x)]
                     (reagent/as-element
                       [nbase/pressable {:m 1
                                         :p 4
                                         :borderRadius "md"
                                         :borderWidth "1"
                                         :borderColor "gray.300"
                                         :bg "white"
                                         :on-press (fn [e] (re-frame/dispatch [:navigate-to :question-detail]))}
                        [nbase/hstack
                         [nbase/vstack
                          [nbase/box {:bg "gray.300"
                                      :borderRadius "md"
                                      :p 4
                                      :alignSelf "center"}]
                          [nbase/box {:alignSelf "center"
                                      :mt 2
                                      :pl 2}
                           [text/measured-text {:fontFamily "MongolianBaiZheng" :fontSize 18 :color "gray.500"}
                             (j/get item :user_name)]]]
                         [nbase/box {:mt 9
                                     :ml 2}
                          [text/measured-text {:fontFamily "MongolianBaiZheng" :fontSize 22 :color "darkBlue.800"}
                            (j/get item :question_content)]]
                         [nbase/box {:mt 9
                                     :ml 1}
                          [text/measured-text {:fontFamily "MongolianBaiZheng" :fontSize 18 :color "gray.500"}
                            (j/get item :question_detail)]]
                         [nbase/box {:ml 2
                                     :mt 9
                                     :h "100%"}
                          [nbase/vstack {:justifyContent "flex-end"}
                           [nbase/icon {:as Ionicons :name "ios-ellipsis-vertical-sharp"
                                        :size "6" :color "indigo.500"
                                        :mb 6}]
                           [nbase/icon {:as Ionicons :name "ios-heart-sharp"
                                        :size "6" :color "indigo.500"
                                        :mb 6}]
                           [nbase/icon {:as Ionicons :name "ios-chatbubble-ellipses-outline"
                                        :size "6" :color "indigo.500"
                                        :mb 6}]
                           [nbase/icon {:as Ionicons :name "time-outline"
                                        :size "6" :color "gray.300"}]
                           [nbase/box {:alignSelf "center" :mt 2}
                            [nbase/measured-text {:color "gray.300"} "2022-04-10 13:52:54"]]]]]])))
     :showsHorizontalScrollIndicator false
     :horizontal true
     :flex 1
     :h "100%"}]
   [nbase/box {:right 4
               :bottom 2}
               ; :position "absolute"}
    [nbase/icon-button {:w 16 :h 16 :borderRadius "full" :variant "solid" :colorScheme "indigo"
                        :justifyContent "center" :alignSelf "center" :alignItems "center"
                        :icon (reagent/as-element [nbase/icon {:as Ionicons :name "ios-add"}])
                        :onPress (fn [e]
                                   (js/console.log "icon-button on press")
                                   (re-frame/dispatch [:navigate-to :question-detail]))}]]])

(def question-list
  {:name       :question-list
   :component  list-view
   :options
   {:title ""}})

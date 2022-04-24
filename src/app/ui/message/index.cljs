(ns app.ui.message.index
  (:require
    [app.ui.nativebase :as nbase]
    [app.ui.editor :as editor]
    [app.ui.components :as ui]
    [app.ui.text :as text]
    [app.ui.keyboard.index :as keyboard]
    [app.ui.keyboard.candidates :as candidates]
    [app.ui.keyboard.bridge :as bridge]

    [steroid.rn.core :as rn]
    [applied-science.js-interop :as j]
    [cljs-bean.core :as bean]
    [reagent.core :as reagent]
    [re-frame.core :as re-frame]

    ["react-native-vector-icons/Ionicons" :default Ionicons]))

(def conversations (reagent/atom [{:name "Mike" :message "can be customized as needed with our utility API."}
                                  {:name "Tom" :message "utilities. Classes are applied to the element itself or sometimes the parent element. These classes "}
                                  {:name "Billy" :message "This is a long paragraph written to show how the line-height of an element is affected by our "}
                                  {:name "Anna" :message "Classes are applied to the element itself or sometimes the parent element."}
                                  {:name "Silly" :message "These classes can be customized as needed with our utility API."}]))

(def messages (reagent/atom {"Mike" [
                                     {:me false :message "I don't understand anything"}
                                     {:me true :message "utilities. Classes are applied to the element itself or sometimes the parent element. These classes "}
                                     {:me true :message "I'm from Hohhot."}
                                     {:me false :message "Where are you from ?"}
                                     {:me true :message "My name is HHH."}
                                     {:me true :message "Hi Mike, Nice to meet you."}
                                     {:me false :message "Hello, My name is Mike."}]}))
(def conversation-name (reagent/atom "Mike"))

(defn edit-view []
  [ui/safe-area-consumer
   [nbase/flex {:flex 1 :justifyContent "space-between"}
    [editor/editor-view
     ;opts
     {:type :text}
     ;content-fn
     (fn []
       identity)
     ;update-fn
     (fn [x]
       identity)]
    [candidates/views]
    [nbase/box {:height 220}
     [keyboard/keyboard]]]])

(defn base-view []
  (let [h (reagent/atom nil)]
    (fn []
     [nbase/zstack {:flex 1
                    :bg "white"
                    :on-layout #(let [height (j/get-in % [:nativeEvent :layout :height])]
                                  (reset! h height))}
      (if (nil? @h)
        [nbase/hstack {:style {:height 674}}
         [nbase/box {:m 1 :p 4
                     :borderRightWidth "0.5"
                     :borderColor "gray.300"
                     :bg "white"}
          [nbase/skeleton {:h "100%" :w 24}]]]
        [nbase/flat-list
         {:keyExtractor    (fn [_ index] (str "conversation-view-" index))
          :data      @conversations
          :renderItem (fn [x]
                        (let [{:keys [item index separators]} (j/lookup x)]
                          (reagent/as-element
                            [nbase/pressable {:style {:height "100%"}; :width 28}}
                                              :on-press (fn [e] (re-frame/dispatch [:navigate-to :message-list])
                                                          (reset! conversation-name (j/get item :name)))}
                             [nbase/vstack
                              [nbase/box {:bg "coolGray.300"
                                          :borderRadius 10
                                          :p 8
                                          :alignSelf "flex-start"
                                          :mb 4}]
                              [nbase/hstack
                               ; [nbase/box {:justifyContent "space-between"}
                               [text/measured-text {:fontSize 18 } (j/get item :name)]
                                ; [text/measured-text {:fontSize 18 } (j/get item :name)]]
                               [text/single-line-text {:fontSize 18 :color "#d4d4d4" :width (- @h 100)} (j/get item :message)]]]])))

          :ItemSeparatorComponent
          (fn [] (reagent/as-element [nbase/divider {:bg "coolGray.200" :thickness "1" :orientation "vertical" :mx "2"}]))
          :p 3
          :initialNumToRender 7
          :showsHorizontalScrollIndicator false
          :horizontal true
          :flex 1
          :h "100%"}])])))

(defn list-view []
  (let [h (reagent/atom nil)
        list-ref (reagent/atom nil)]
    (fn []
      [ui/safe-area-consumer
       [nbase/box {:flex 1}
        [nbase/box {:flex 1
                    :bg "white"
                    :on-layout #(let [height (j/get-in % [:nativeEvent :layout :height])]
                                  (reset! h height))}
         (if (nil? @h)
           [nbase/hstack; {:style {:height 674}}
            [nbase/box {:m 1 :p 4
                        :borderRightWidth "0.5"
                        :borderColor "gray.300"
                        :bg "white"}
             [nbase/skeleton {:h "100%" :w 24}]]]
           [nbase/flat-list
            {:keyExtractor    (fn [_ index] (str "message-view-" index))
             :data     (get @messages @conversation-name)
             :renderItem (fn [x]
                           (let [{:keys [item index separators]} (j/lookup x)]
                             (reagent/as-element
                               (if (true? (j/get item :me))
                                 [nbase/vstack {:justifyContent "flex-end"
                                                :style {:height (- @h 0)
                                                        :paddingHorizontal 8}}
                                  [nbase/box {:p 2 :bg "darkBlue.100"
                                              :borderRightRadius "20"
                                              :borderTopLeftRadius "20"}
                                   [text/measured-text {:fontSize 18 :width (- @h 0 64 60)} (j/get item :message)]]
                                  [nbase/box {:bg "coolGray.300"
                                              :borderRadius 10
                                              :p 8
                                              :mt 4
                                              :alignSelf "flex-start"}]]
                                 [nbase/vstack {:justifyContent "flex-start"
                                                :style {:height (- @h 0)}}
                                  [nbase/box {:bg "coolGray.300"
                                              :borderRadius 10
                                              :p 8
                                              :alignSelf "flex-start"
                                              :mb 4}]
                                  [nbase/box {:p 2 :bg "darkBlue.100"
                                              :borderRightRadius "20"
                                              :borderBottomLeftRadius "20"}
                                   [text/measured-text {:fontSize 18 :width (- @h 0 64 60)} (j/get item :message)]]]))))

             :showsHorizontalScrollIndicator false

             ; :inverted true
             ; :legacyImplementation true
             ; :pagingEnabled true
             :nestedScrollEnabled true
             ; :snapToEnd true
             ; :initialScrollIndex 0
             :inverted true
             ;:style {:flexDirection "row" :height @h}

             :onEndReached (fn [e] (js/console.log "onEndReached >>>>> "))
             :onEndReachedThreshold 2

             :horizontal true
             :flex 1}])]
             ; :h "100%"}])
        [nbase/box {:bg "warmGray.100"
                    :h 12
                    :mt 1
                    :p 2
                    :flexDirection "row"
                    :justifyContent "space-around"}
         [rn/touchable-highlight {:style {:height 32 :width 32 :borderWidth 0.2 :borderRadius 32 :borderColor "#57534e"
                                          :alignItems "center" :justifyContent "center"
                                          :flex 1}
                                  :on-press #(js/console.log "on keypad >>>>")}
          [ui/ion-icons {:name "ios-keypad-sharp" :color "#78716c" :size 18}]]
         [rn/touchable-highlight {:style {:height 32 :borderWidth 0.2 :borderRadius 32 :borderColor "#57534e"
                                          :marginHorizontal 8
                                          :alignItems "center" :justifyContent "center"
                                          :flex 5}
                                  :on-press #(js/console.log "on mic >>>>")}
          [ui/ion-icons {:name "ios-mic" :color "#78716c" :size 18}]]
         [rn/touchable-highlight {:style {:height 32 :width 32 :borderWidth 0.2 :borderRadius 32 :borderColor "#57534e"
                                          :alignItems "center" :justifyContent "center"
                                          :flex 1}
                                  :on-press #(js/console.log "on add >>>>")}
          [ui/ion-icons {:name "ios-add" :color "#78716c" :size 18}]]]]])))

(def model-edit
  {:name       :message-edit
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

(def model-list
  {:name       :message-list
   :component  list-view
   :options
   {:title ""}})

(def model-base
  {:name       :message-base
   :component  base-view
   :options
   {:title ""}})

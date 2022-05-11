(ns app.ui.question.index
  (:require
    [app.ui.nativebase :as nbase]
    [app.ui.editor :as editor]
    [app.ui.components :as ui]
    [app.ui.text :as text]
    [app.ui.keyboard.index :as keyboard]
    [app.ui.keyboard.candidates :as candidates]
    [app.ui.keyboard.bridge :as bridge]
    [app.ui.refresh :as refresh]
    [app.handler.gesture :as gesture]
    [app.handler.animated :as animated]
    [app.handler.animation :as animation]
    [app.handler.animatable :as animatable]
    ["react-native-gesture-handler" :as ges]
    ["react-native-reanimated" :as re-animated]
    ["lottie-react-native" :as lottie]

    [steroid.rn.core :as srn]
    [applied-science.js-interop :as j]
    [cljs-bean.core :as bean]
    [reagent.core :as reagent]
    [re-frame.core :as re-frame]

    ["react-native" :as rn :refer [Dimensions]]
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
  (let [h (reagent/atom nil)
        scroll-ref (atom nil)
        is-loading (atom false)
        container-offset (new rn/Animated.ValueXY #js {:x 0 :y 0})
        transform-offset (new rn/Animated.ValueXY (bean/->js {:x (- 80) :y 0}))
        scroll-enabled (atom false)
        inner-scroll-offset (atom 0)

        ;
        left-pull-threshold 2
        threshold 80
        reset-container-position (fn []
                                   (let [timing (animation/animated-timing transform-offset #js{:toValue -80
                                                                                                :duration 250
                                                                                                :useNativeDriver true})]
                                     (j/call timing :start))
                                   (let [timing (animation/animated-timing container-offset #js{:toValue 0
                                                                                                :duration 250
                                                                                                :useNativeDriver true})]
                                     (j/call timing :start)))
        on-refresh (fn []
                     (js/console.log "on-refresh .... ")
                     (js/setTimeout
                       reset-container-position
                       2000))
        is-innerscroll-left (<= @inner-scroll-offset left-pull-threshold)
        check-scroll (fn []
                       (if (is-innerscroll-left)
                         (if (true? @scroll-enabled)
                           (reset! scroll-enabled false))
                         (if (false? @scroll-enabled)
                           (reset! scroll-enabled true))))

        on-scroll (fn [e]
                    (let [x (j/get-in e [:nativeEvent :contentOffset :x])]
                      (reset! inner-scroll-offset x)))
                      ; (check-scroll)))
        ;
        pan-responder
        (rn/PanResponder.create
          #js { ;:onStartShouldSetPanResponder (fn [arg] true)
                ; :onStartShouldSetPanResponder (fn [e state] true)
                ; :onStartShouldSetPanResponderCapture (fn [e state] true)
                ; :onMoveShouldSetPanResponder (fn [e state] true)
                ; :onPanResponderReject (fn [e state] (js/console.log "onPanResponderReject ... "))
                :onMoveShouldSetPanResponderCapture (fn [e state]
                                                      ; (js/console.log "onMoveShouldSetPanResponderCapture start ...")
                                                      ; true)
                                                      (let [result
                                                             (cond
                                                               (true? @is-loading) false

                                                               ; (false? @scroll-enabled) false

                                                               (>= @inner-scroll-offset left-pull-threshold)
                                                               false

                                                               (< 0 (j/get state :dx))
                                                               true

                                                               :else
                                                               false)]
                                                        ; (js/console.log "onMoveShouldSetPanResponderCapture end ..." result)
                                                        result))
                :onPanResponderMove (fn [e state]
                                      (let [x (j/get state :dx)]
                                        ; (js/console.log "onPanResponderMove start ..." state " container-offset = " container-offset " x = " x)
                                        ;
                                        (if (> x 0)
                                          (when (<= x 80)
                                            (.setValue ^js container-offset #js {:x (j/get state :dx)
                                                                                 :y (j/get state :dy)})
                                            (.setValue ^js transform-offset (bean/->js {:x (- x 80) :y 0})))
                                          (do
                                            (.setValue ^js container-offset #js {:x 0 :y 0})
                                            (.setValue ^js transform-offset (bean/->js {:x -80 :y 0}))))))
                :onPanResponderRelease (fn [e state]
                                         (js/console.log "onPanResponderRelease ..." state (j/get-in container-offset [:x :_value]))
                                         (if (<= threshold (j/get state :dx))
                                           (on-refresh)
                                           (reset-container-position)))
                                         ; (check-scroll))
                :onPanResponderTerminationRequest (fn [e state] false)
                :onPanResponderTerminate (fn [e state]
                                           (js/console.log "onPanResponderTerminate ..." state)
                                           (reset-container-position))
                                         ; (check-scroll))
                :onShouldBlockNativeResponder (fn [e] true)})]

    (fn []
      [nbase/zstack {:flex 1
                    ; :bg "gray.100"
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
         [animation/animated-view
          (merge (bean/->clj (j/get pan-responder :panHandlers))
                 {:style {:flex 1
                          :flexDirection "row"}})
          [animation/animated-view
           {:style {:flex 1 :transform [{:translateX (j/get container-offset :x)}]}}
           [nbase/flat-list
            {:keyExtractor    (fn [_ index] (str "question-view-" index))
             :data      @questions-atom
             :overScrollMode "never"
             :scrollToOverflowEnabled true
             :onScroll (fn [e]
                         (reset! inner-scroll-offset (j/get-in e [:nativeEvent :contentOffset :x])))
             :ref (fn [r]
                    (reset! scroll-ref r))
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
                                 [text/simple-text {:fontFamily "MongolianBaiZheng" :fontSize 18 :color "#71717a"
                                                    :width (- @h 20)}
                                  (j/get item :question_detail)]]
                                [nbase/box {:ml 2
                                            :mt 9
                                            :style {:height (- @h 120)}
                                            :justifyContent "space-between"}
                                 [nbase/box
                                  [nbase/icon {:as Ionicons :name "ios-ellipsis-vertical-sharp"
                                               :size "6" :color "indigo.500"
                                               :mb 40}]]
                                 [nbase/box {:justifyContent "flex-start"}
                                  [nbase/box {:mb 6 :alignItems "center"}
                                   [nbase/icon {:as Ionicons :name "ios-heart-sharp"
                                                :size "6" :color "indigo.500"}]
                                   [text/measured-text {:color "#d4d4d8"} "1024"]]
                                  [nbase/box {:mb 6 :alignItems "center"}
                                   [nbase/icon {:as Ionicons :name "ios-chatbubble-ellipses-outline"
                                                :size "6" :color "indigo.500"}]
                                   [text/measured-text {:color "#d4d4d8"} "128"]]
                                  [nbase/icon {:as Ionicons :name "time-outline"
                                               :size "6" :color "gray.300"
                                               :mb 1}]
                                  [nbase/box {:alignSelf "center"}
                                   [text/measured-text {:color "#d4d4d8"} "2022-04-10 13:52:54"]]]]]])))
             :showsHorizontalScrollIndicator false
             :horizontal true
             :bounces true
             :style {:flex 1 :height "100%"
                     :width (.-width (.get Dimensions "window"))}}]]])
       ;
       [animation/animated-view
        {:style {:width 80
                 :height "100%"
                 :transform [{:translateX (j/get transform-offset :x)}]}}
        ; [nbase/box {:flex 1 :bg "primary.100"}]
        [:> lottie
           {:source (js/require "../src/json/104547-loading-25.json")
            :autoPlay true}]]
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
  (let [h (reagent/atom 0)]
    (fn []
      [ui/safe-area-consumer
       [nbase/flex {:flex 1
                    :p 5
                    :flex-direction "row"
                    :bg "white"
                    :on-layout #(let [height (j/get-in % [:nativeEvent :layout :height])]
                                  (reset! h height))}

        [nbase/box {:p 2}
         [text/measured-text {:fontSize 18 :fontFamily "MongolianBaiZheng"} "ᠠᠰᠠᠭᠤᠯᠲᠠ"]]
        [srn/touchable-highlight {:style {:borderWidth 1 :borderColor "#06b6d4"
                                          :paddingHorizontal 8
                                          :paddingVertical 20
                                          :borderRadius 8}
                                   :underlayColor "#cccccc"
                                   :on-press (fn [] (reset! active-key :question_content)
                                               (re-frame/dispatch [:navigate-to :question-edit]))}
         [text/measured-text {:fontSize 18 :fontFamily "MongolianBaiZheng"} (:question_content @model)]]
        [nbase/box {:p 2 :ml 2}
         [text/measured-text {:fontSize 18 :fontFamily "MongolianBaiZheng"} "ᠲᠠᠢᠯᠪᠤᠷᠢ"]]
        (if-not (zero? @h)
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
            [text/measured-text {:fontSize 18 :fontFamily "MongolianBaiZheng" :width (- @h 40)} (:question_detail @model)]]])]])))



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
    [candidates/views {}]
    [nbase/box {:height 220}
     [keyboard/keyboard {}]]]])

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

(ns app.ui.nativebase
  (:require
   [applied-science.js-interop :as j]
   [cljs-bean.core :as bean]
   [reagent.core :as reagent]
   [steroid.rn.core :as rn]
   [steroid.rn.navigation.core :as rnn]
   [app.ui.text.index :as text]
   [promesa.core :as p]
   [promesa.exec :as exec]
   ["react-native-measure-text-chars" :as rntext]
   ["native-base" :refer [NativeBaseProvider
                          Center
                          Container
                          Box
                          Modal Modal.Content Modal.CloseButton Modal.Header Modal.Body Modal.Footer
                          Heading

                          Alert Alert.Icon

                          Text
                          Button Button.Group
                          CloseIcon
                          IconButton

                          Badge
                          Pressable
                          CheckIcon
                          Select Select.Item
                          Actionsheet Actionsheet.Content Actionsheet.Item

                          Spacer
                          Divider
                          HStack
                          VStack
                          ZStack
                          Flex

                          FlatList
                          ScrollView

                          Collapse
                          Spinner

                          useStyledSystemPropsResolver
                          usePropsResolution
                          useThemeProps
                          useToast
                          useDisclose]]
   ["react" :as react]))


(def nativebase-provider (reagent/adapt-react-class NativeBaseProvider))

(def box (reagent/adapt-react-class Box))
(def badge (reagent/adapt-react-class Badge))

(def container (reagent/adapt-react-class Container))

(def heading (reagent/adapt-react-class Heading))

(def text (reagent/adapt-react-class Text))

(def button (reagent/adapt-react-class Button))
(def button-group (reagent/adapt-react-class Button.Group))

(def icon-button (reagent/adapt-react-class IconButton))
(def close-icon (reagent/adapt-react-class CloseIcon))

(def select (reagent/adapt-react-class Select))
(def select-item (reagent/adapt-react-class Select.Item))
(def alert (reagent/adapt-react-class Alert))
(def alert-icon (reagent/adapt-react-class Alert.Icon))
(def collapse (reagent/adapt-react-class Collapse))
(def spinner (reagent/adapt-react-class Spinner))
; Modal Modal.Content Modal.CloseButton Modal.Header Modal.Body Modal.Footer))
(def modal (reagent/adapt-react-class Modal))
(def modal-content (reagent/adapt-react-class Modal.Content))
(def modal-close-button (reagent/adapt-react-class Modal.CloseButton))
(def modal-header (reagent/adapt-react-class Modal.Header))
(def modal-body (reagent/adapt-react-class Modal.Body))
(def modal-footer (reagent/adapt-react-class Modal.Footer))

; Actionsheet Actionsheet.Content Actionsheet.Item
(def actionsheet (reagent/adapt-react-class Actionsheet))
(def actionsheet-content (reagent/adapt-react-class Actionsheet.Content))
(def actionsheet-item (reagent/adapt-react-class Actionsheet.Item))
(def checkicon (reagent/adapt-react-class CheckIcon))

(def spacer (reagent/adapt-react-class Spacer))

(def pressable (reagent/adapt-react-class Pressable))

(def flex (reagent/adapt-react-class Flex))

(def hstack (reagent/adapt-react-class HStack))
(def vstack (reagent/adapt-react-class VStack))
(def zstack (reagent/adapt-react-class ZStack))
(def divider (reagent/adapt-react-class Divider))
(def scroll-view (reagent/adapt-react-class ScrollView))
(def flat-list (reagent/adapt-react-class FlatList))

(def center (reagent/adapt-react-class Center))

(defn text-view [props text]
  (let [[props _] (useStyledSystemPropsResolver (bean/->js props))]
    [text/text-view (merge {:text text}
                            ; :height "auto"}
                         (bean/->clj props))]))

(defn gradient-view []
  (let [text-props {:fontSize "md" :color :white}
        [props _] (useStyledSystemPropsResolver (bean/->js text-props))]
    [center {:flex 1 :px 3 :safeArea true}
     [box {:bg {:linearGradient {:colors ["lightBlue.300" "violet.800"]
                                 :start [0 0]
                                 :end [1 0]}}
           :p "12"
           :rounded "lg"
           :_text text-props
           :safeArea true}
      [text/text-view (merge {:text
                               "This is a Box with Linear Gradient"
                              :height "auto"}
                             (bean/->clj props))]]]))

(defn toast-render [props text]
  (bean/->js
    {:render
     (fn []
       (reagent/as-element
         [box {:bg "emerald.500" :px "2" :py "1" :rounded "sm" :mb 5}
          [text/text-view (merge {:text
                                  text
                                  :height "auto"}
                                (bean/->clj props))]]))
     :placement :bottom-left}))

; (defn view []
(defn toast-view []
  (let [text-props {:fontSize "md" :color :white}
        [props _] (useStyledSystemPropsResolver (bean/->js text-props))
        toast (useToast)]
    (js/console.log "props = " (bean/->js props))
    [center {:flex 1 :px 3 :safeArea true}
     [pressable
      {:onPress (fn []
                  (j/call toast
                       ; :show (toast-render props "Hello! Have a nice day")))}
                           :show (toast-render props "ᠠᠰᠠᠭᠤᠳᠠᠯ ᠲᠠᠢ ᠠᠩᠬᠠᠷ")))}

      [box {:bg "emerald.500" :p "2" :rounded "sm"}
       [:f> text-view (merge
                        {:height "auto"}
                        (bean/->clj props))
        "Custom Toast"]]]]))

(defn pressable-view []
; (defn view []
  [center {:flex 1 :py 3 :safeArea true}
   [pressable
    (fn [x]
      (let [{:keys [isHovered isFocused isPressed]} (j/lookup x)]
        (if isPressed
          (js/console.log ">>>>>>>"))
        (reagent/as-element
         [box {:bg (cond
                     isPressed "cyan.900"
                     isHovered "cyan.800"
                     :else "cyan.700")
               :p "5"
               :rounded "8"
               :style {:transform [{:scale (if isPressed 0.96 1)}]}}
          [hstack {:alignItems "flex-start"}
           [vstack
            [:f> text-view {:height "auto" :fontSize 12 :color "cyan.50" :fontWeight "medium"} "Business"]
            [spacer]
            [:f> text-view {:height "auto" :fontSize 10 :color "cyan.100"} "1 month ago"]]
           [:f> text-view {:height "auto" :width 400 :color "cyan.50" :ml "3" :fontWeight "medium" :fontSize 20}
              "Marketing License"]
           [spacer]
           [:f> text-view {:height "auto" :ml "2" :fontSize 14 :color "cyan.100"}
            "Unlock powerfull time-saving tools for"]
           [spacer]
           [:f> text-view {:height "auto" :ml "2" :fontSize 14 :color "cyan.100"}
            "creating email delivery and collecting"]
           [spacer]
           [:f> text-view {:height "auto" :ml "2" :fontSize 14 :color "cyan.100"}
            "marketing data"]
           [flex
            (if isFocused
              [:f> text-view {:height "auto"
                              :ml "2"
                              :fontSize 12
                              :fontWeight "medium"
                              :bg "cyan.500"
                              :color "cyan.200"
                              :alignSelf "flex-start"}
                "Read More"]
              [:f> text-view {:height "auto" :ml "2"
                              :fontSize 12 :fontWeight "medium" :color "cyan.400"}
               "Read More"])]]])))]])

(defn button-view [props text]
  (let [text-props (bean/->js (useThemeProps "Button" (bean/->js props)))]
    [button props
     [:f> text-view (merge {:height "auto"} (:_text (bean/->clj text-props)))
      text]]))

(defn modal-view []
; (defn view []
  (let [[modalVisible, setModalVisible] (.useState react false)]
    [center {:flex 1 :py 3 :safeArea true}
     [button {:onPress #(setModalVisible true)} "Button"]
     [modal {:isOpen modalVisible :onClose setModalVisible}
      [modal-content {:maxWidth "80%" :maxH "300px" :style {:flexDirection "row"}}
       ; [modal-close-button]
       [modal-header
        [:f> text-view {:height "auto" :ml "2"
                        :fontSize 12 :fontWeight "medium" :color "cyan.400"}
         "Contact Us"]]
       [modal-body
        ; [scroll-view]
        ; [text "Create a 'Return Request' under “My Orders” section of App/Website. Follow the screens that come up after tapping on the 'Return’ button. Please make a note of the Return ID that we generate at the end of the process. Keep the item ready for pick up or ship it to us basis on the return mode."]
        [:f> text-view {:height 250} "Create a 'Return Request' under “My Orders” section of App/Website. Follow the screens that come up after tapping on the 'Return’ button. Please make a note of the Return ID that we generate at the end of the process. Keep the item ready for pick up or ship it to us basis on the return mode."]]
       [modal-footer
        [button-group {:space 2 :direction "column"}
         [:f> button-view {:variant "ghost"
                           :colorScheme "blueGray"
                           :onPress #(setModalVisible false)
                           :on-layout (fn [x] (js/console.log "on-layout >>>>"))}
           "Cancel"]
         [:f> button-view {}
           "Save"]]]]]]))

(defn actionsheet-item-view [props text]
  (let [text-p (bean/->js (useThemeProps "ActionsheetItem" (bean/->js props)))
        [text-props _] (useStyledSystemPropsResolver (bean/->js (j/get text-p :_text)))]
    [actionsheet-item props
     [:f> text-view (merge {:height "auto"} (if (:height props) {:height (:height props)}) (bean/->clj text-props))
      text]]))

(defn actionsheet-view []
; (defn view []
  (let [disclose-props (useDisclose)
        {:keys [isOpen onOpen onClose]} (j/lookup disclose-props)]
    [center {:flex 1 :py 3} ;:safeArea true}
     [:f> button-view {:onPress onOpen} "ActionSheet"]
     [actionsheet {:isOpen isOpen :onClose onClose :safeArea true}
      [actionsheet-content {:maxH 300}
       [container {:flexDirection "row"}
        [box {:h "100%" :w 12 :py 4 :justifyContent "center"}
         [:f> text-view {:fontSize "16"
                         :color "gray.500"
                         :_dark {:color "gray.300"}}
           "Albums"]]
        [:f> actionsheet-item-view {:height 250 :h "100%" :w 12 :py 4} "Delete"]
        [:f> actionsheet-item-view {:height 250 :h "100%" :w 12 :py 4} "Share"]
        [:f> actionsheet-item-view {:height 250 :h "100%" :w 12 :py 4} "Play"]
        [:f> actionsheet-item-view {:height 250 :h "100%" :w 12 :py 4} "Favourite"]
        [:f> actionsheet-item-view {:height 250 :h "100%" :w 12 :py 4} "Cancel"]]]]]))

(defn select-item-view [props text]
  (let [text-props (bean/->js (useThemeProps "ActionsheetItem" (bean/->js props)))]
    [select-item (merge {:label (reagent/as-element
                                 [:f> text-view (merge (:_text (bean/->clj text-props)) {:height 250})  text])}
                                 ; [:f> text-view {:height 250}  text])}
                      props)]))


(defn select-view2 []
; (defn view []
  (let [[service, setService] (.useState react "")
        [label setLabel] (.useState react "")]
    [center {:flex 1 :py 3 :safeArea true}
      [vstack {:alignItems "center" :space 4}
       [select {;:selectedValue "web"
                :minWidth "200"
                :accessibilityLabel "Web Development"
                :placeholder "Choose Service"
                :_selectedItem
                {
                  :bg "teal.600"
                  :endIcon (reagent/as-element [checkicon {:size "5"}])}
                :onValueChange #(js/console.log ">>>> " %)
                :mt 1}
                ; :_actionSheetContent}
                ; {:flexDirection "row"}}

        [container {:flexDirection "row"}
         [box {:height "250" :w 14 :py 4 :justifyContent "center"}
          [:f> text-view {:fontSize "16"
                          :color "gray.500"
                          :_dark {:color "gray.300"}}
            "Development"]]
         [:f> select-item-view {:value "ux" :_stack { :direction "column"} :w 12} "UX Research"]
         [:f> select-item-view {:value "web" :_stack { :direction "column"} :w 12} "Web Development"]
         [:f> select-item-view {:value "cross" :_stack { :direction "column"} :w 12} "Cross Platform Development"]]]]]))
         ; [select-item {:label (reagent/as-element
         ;                       [:f> text-view (merge (bean/->clj text-props) {:height 250 :w 12 :py 4})  "UX Research"])
         ;               :value "ux" :_stack { :direction "column"} :w 12}]]]]]))
         ; [select-item {:label (reagent/as-element
         ;                        [:f> text-view {:height 250 :w 12 :py 4} "Web Development"])
         ;               :value "web" :_stack { :direction "column"} :w 12}]
         ; [select-item {:label (reagent/as-element
         ;                        [:f> text-view {:height 250 :w 12 :py 4} "Cross Platform Development"])
         ;               :value "cross" :_stack { :direction "column"} :w 12}]
         ; [select-item {:label (reagent/as-element
         ;                        [:f> text-view {:height 250 :w 12 :py 4} "UI Designing"])
         ;               :value "ui" :_stack { :direction "column"} :w 12}]
         ; [select-item {:label (reagent/as-element
         ;                        [:f> text-view {:height 250 :w 12 :py 4} "Backend Development"])
         ;               :value "backend" :_stack { :direction "column"} :w 12}]]]]]))



(defn rotated-text [props width height t]
  (let [offset (js/Math.abs (- (/ height 2) (/ width 2)))]
    [text (merge props {:style {:width height :height width
                                :transform [{:rotate "90deg"}
                                            {:translateX offset}
                                            {:translateY offset}]}})
      t]))

(defn line-height [font-size]
  (* font-size
    (if (> font-size 20) 1.5 1)))

(defn measured-text
  ([props t]
   (let [width (:height props)
         info (rntext/measure (bean/->js (merge (assoc props :text t) (if width {:width width}))))]
     (measured-text props t (bean/->clj info))))
  ([props t info]
   (let [height (or (:height props) (:width info))
         width (/ (:height info) (:lineCount info))
         offset (- (/ height 2) (/ width 2))]
     (cond
       (nil? info)
       [text "empty ...."]

       (= 1 (:lineCount info))
       [box {:style {:width (:height info)
                     :height height}}
        [rotated-text props width height t]]

       :else
       [box {:style {:width (:height info)
                     :height height}}
        [flat-list
         {:horizontal true
          :keyExtractor    (fn [_ index] (str "text-" index))
          :data (map (fn [x] (subs t (:start x) (:end x))) (:lineInfo info))
          :renderItem
          (fn [x]
            (let [{:keys [item index separators]} (j/lookup x)]
              (reagent/as-element
                [box {:width width :height height}
                 [rotated-text props width height item]])))}]]))))

(defn track-text [props t info]
  (let [tt @(reagent/track (fn [] t))]
    (p/then (rntext/measure
             (bean/->js (merge props {:text @t})))
      (fn [result]
        (reset! info (bean/->clj result))))
    tt))

(defn measured-view []
; (defn view []
  (let [value (reagent/atom "ZZ Hello abc, This is the normal text!")
        info (reagent/atom nil)
        vv @(reagent/track track-text {:fontSize 14 :width 100} value info)]
    (fn []
      [center {:flex 1 :py 3 :safeArea true}
       [box {}
        [button {:onPress #(reset! value "Hello abc, This is the normal text!")}
         "button1"]
        [button {:onPress #(reset! value "Hello def, This is test normal text for display vertical")}
         "button2"]
        ; [text @value]]])))
        [measured-text {:fontSize 14 :width 100} @value @info]]])))

(defn actionsheet-item-view2 [props text]
  (let [text-p (bean/->js (useThemeProps "ActionsheetItem" (bean/->js props)))
        [text-props _] (useStyledSystemPropsResolver (bean/->js (j/get text-p :_text)))]
    [actionsheet-item props
     [measured-text (merge (bean/->clj text-props) {:height 250 :width 12})
      text]]))

(declare select-view3)
(defn select-view [value]
; (defn view []
  (let [props (useThemeProps "Input" #js {})
        text-props {:color "#1f2937" :fontSize  12}
        props (bean/->clj props)]
    (js/console.log (:placeholderTextColor props))
    (js/console.log (bean/->js props))
    [select-view3 props text-props]))

(defn select-view3 [props text-props]
  (let [value (reagent/atom "")
        isopen (reagent/atom false)
        press-fn (fn [t] (reset! value t) (reset! isopen false))
        item-props (fn [t]
                     (merge
                       {:height 250 :w 12 :py 4 :onPress (fn [] (press-fn t))}
                       (if (= @value t)
                         {
                          :bg "teal.600"
                          :endIcon (reagent/as-element [checkicon {:size "5"}])})))]
    (fn []
      [center {:flex 1 :py 3 :safeArea true}
       [box (merge {:borderWidth 1 :borderColor "transparent"} props)
        [pressable {:onPress (fn []
                                (reset! isopen true))
                    :accessibilityLabel "button1"
                    :accessibilityRole "button"}
         (if (empty? @value)
           [measured-text (merge text-props {:height 300 :width 16 :color (:placeholderTextColor props)}) "Choose Service"]
           [measured-text (merge text-props {:height 300 :width 16}) @value])]
        [actionsheet {:isOpen @isopen :onClose #(reset! isopen false) :safeArea true}
         [actionsheet-content {:maxH 300}
          [container {:flexDirection "row"}
           [box {:h "100%" :w 12 :py 4 :justifyContent "center"}
            [:f> text-view {:fontSize "16"
                            :color "gray.500"
                            :_dark {:color "gray.300"}}
              "Development"]]
           [:f> actionsheet-item-view {:height 250 :w 12 :py 4 :onPress (fn [] (press-fn "UX Research"))} "UX Research"]
           [:f> actionsheet-item-view {:height 250 :w 12 :py 4 :onPress (fn [] (press-fn "Web Development"))} "Web Development"]
           [:f> actionsheet-item-view {:height 250 :w 12 :py 4 :onPress (fn [] (press-fn "Cross Platform Development"))} "Cross Platform Development"]
           [:f> actionsheet-item-view {:height 250 :w 12 :py 4 :onPress (fn [] (press-fn "UI Designing"))} "UI Designing"]
           [:f> actionsheet-item-view {:height 250 :w 12 :py 4 :onPress (fn [] (press-fn "Backend Development"))} "Backend Development"]]]]]])))

; (defn view [])
(defn select3-view []
  [:f> select-view])

(defn theme-text-props [name props]
  (let [theme-props (bean/->js (useThemeProps name (bean/->js props)))
        [text-props _] (useStyledSystemPropsResolver (bean/->js (j/get theme-props :_text)))]
    text-props))

(defn theme-props [name props]
  (let [theme-props (bean/->js (useThemeProps name (bean/->js props)))
        [text-props _] (useStyledSystemPropsResolver (bean/->js theme-props))]
    (bean/->clj text-props)))

(defn badge-view [props t]
  (let [text-props (theme-text-props "Badge" props)]
    [badge props
     [measured-text (bean/->clj text-props)  t]]))

(defn styled-text-view [props t]
  (let [[text-props _] (useStyledSystemPropsResolver (bean/->js props))]
    [measured-text (bean/->clj text-props) t]))

(defn styled-button-view [props t]
  (let [text-props (bean/->clj (theme-text-props "Button" props))]
    [button props
     [measured-text text-props t]]))


(defn multi-badge-view []
  [hstack {:space {:base "2" :md "4"}
           :mx {:base "auto" :md "0"}}
   (for [item ["solid" "outline" "subtle"]]
     [vstack {:key item :space 4}
      [:f> badge-view {:py 4 :alignSelf "center" :variant item} "DEFAULT"]
      [:f> badge-view {:py 4 :colorScheme "success" :variant item :alignSelf "center"} "SUCCESS"]
      [:f> badge-view {:py 4 :colorScheme "danger" :variant item :alignSelf "center"} "DANGER"]
      [:f> badge-view {:py 4 :colorScheme "info" :variant item :alignSelf "center"} "INFO"]])])

(defn alert-view []
  (let [show (reagent/atom false)]
    (fn []
      [box
       [collapse {:isOpen @show}
        [alert {};:status "default"}
         [hstack {:space 1 :flexShrink 1}
          [vstack {:space 1}
           [alert-icon]
           [:f> styled-text-view {:fontSize "md" :_dark {:color "coolGray.800"}} "Please try again later!"]]
          [box {:pl "6" :pt "2"}
           [:f> styled-text-view {:height 150} "Your coupon could not be processed at this time."]]
          [icon-button {:variant "unstyled"
                        :icon (reagent/as-element [close-icon {:color "coolGray.600" :size 3}])
                        :onPress #(reset! show false)}]]]]
       [:f> styled-button-view {:size "sm" :onPress #(reset! show true) :my 8 :mx "auto"} "Re-Open"]])))

(defn progress-view [p]
  ;; {:height 100 :w "sm"}
  (let [props (bean/->clj (usePropsResolution "Progress" (bean/->js (dissoc p :height :width :value))))
        min-num (or (:min p) (:min props))
        max-num (or (:max p) (:max props))
        value (:value p)
        h (cond
            (> max-num value min-num)
            (str (* 100.0 (/ (- value min-num) (- max-num min-num) 1.0)) "%")
            (> value min-num) "100%"
            :else "0%")]
    [box (merge props p)
      [box (merge (:_filledTrack props) {:width "100%" :height h})]]))

(defn multi-progress-view []
  [hstack {:space "md"}
   (for [item ["primary" "secondary" "emerald" "warning" "light"]]
     ^{:key item}
     [:f> progress-view {:height 200 :width "2" :value 77 :colorScheme item}])])

(defn spinner-view [p]
  (let [props (theme-props "Heading" (merge {:color "primary.500" :fontSize "md"} p))]
  ; (let [props (useThemeProps "Heading" (bean/->js {:color "primary.500" :fontSize "md"}))]
    [vstack {:space 2 :alightItems "center"}
     [spinner p]
     [measured-text props "Loading"]]))

(defn multi-spinner-view []
  [hstack {:space "md"}
   (for [item ["emerald.500" "warning.500" "indigo.500" "cyan.500"]]
     ^{:key item}
     [:f> spinner-view {:color item}])])

(defn view []
  [center {:flex 1 :py 3 :safeArea true}
   [multi-spinner-view]])

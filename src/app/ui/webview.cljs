(ns app.ui.webview
  (:require
   [cljs-bean.core :as bean]
   [app.ui.nativebase :as nbase]
   [app.ui.components :as ui]
   [app.ui.html :as html]
   [app.handler.gesture :as gesture]
   [reagent.core :as reagent]
   [applied-science.js-interop :as j]
   ; ["@react-native-clipboard/clipboard" :default Clipboard]
   ["react-native" :as rn :refer [Dimensions Clipboard]]
   ["react-native-webview" :refer [WebView]]
   ["react-native-smooth-blink-view" :default blinkview]
   ["react-native-svg" :as svg])
  (:import
   [goog.async Debouncer]))

(def platform (j/get-in rn [:Platform :OS]))

(def font-url
  (condp = platform
    "android" "file:///android_asset/fonts/MongolianBaiZheng.ttf"
    "ios" "MongolianBaiZheng.ttf"))

; src:local('MongolianBaiZheng')  url('" font-url "') format('truetype')
;ios >>>>>>
;src: local('MongolianBaiZheng'), url('" font-url "');
(def css (str "<style>
  @font-face {
     font-family: 'MongolianBaiZheng';
     src: local('MongolianBaiZheng'), url('" font-url "');
  }
      body {font-family: MongolianBaiZheng; caret-color: black; writing-mode: vertical-lr; -webkit-text-orientation: sideways-right;overflow-x: hidden; -webkit-overflow-scrolling: touch;}
      .pell { height: 100%; width: 100%;} .pell-content { outline: 0; overflow-x: auto;padding: 10px;width: 100%; -webkit-overflow-scrolling: touch;}
      video {max-width: 98%;margin-left:auto;margin-right:auto;display: block;}
      img {max-width: 98%;vertical-align: middle;}
      table {height: 100% !important;}
      table td {height: inherit;}
      table span { font-size: 12px !important; }
      .x-todo li {list-style:none;}
      .x-todo-box {position: relative; left: -24px;}
      .x-todo-box input{position: absolute;}
      blockquote{border-top: 6px solid #ddd;padding: 10px 5px 0 5px;margin: 15px 15px 0 15px;}
      hr{display: block;width: 0; border: 0;border-left: 1px solid #ccc; margin: 0 15px; padding: 0;}
  </style>"))

(def content "
<br/>

<h1>aaaaᠪᠢ ᠮᠣᠩᠭᠤᠯ ᠬᠥᠮᠦᠨ</h1>
<h2>ᠴ᠂ ᠴᠢᠮᠡᠳ</h2>
<p>ᠠᠷᠭᠠᠯ ᠤᠨ ᠤᠲᠤᠭ᠎ᠠ ᠪᠤᠷᠭᠢᠯᠤᠭᠰᠠᠨ</p>
<p>ᠮᠠᠯᠴᠢᠨ ᠤ ᠭᠡᠷ᠎ ᠲᠥ ᠲᠥᠷᠦᠭᠰᠡᠨ ᠪᠢ</p>
<p>ᠠᠲᠠᠷ ᠬᠡᠭᠡᠷ᠎ᠡ ᠨᠤᠲᠤᠭ  ᠢᠢᠡᠨ</p>
<p>ᠥᠯᠦᠭᠡᠢ᠎ᠮᠢᠨᠢ ᠭᠡᠵᠦ ᠪᠣᠳᠤᠳᠠᠭ᠃</p>
<p>&nbsp;</p>
<p>ᠴᠡᠩᠬᠡᠷ ᠮᠠᠨᠠᠨ ᠰᠤᠭᠤᠨᠠᠭᠯᠠᠭᠰᠠᠨ</p>
<p>ᠠᠯᠤᠰ᠎ ᠤᠨ ᠪᠠᠷ᠎ᠠ᠎ ᠶᠢ ᠰᠢᠷᠲᠡᠭᠡᠳ</p>
<p>ᠴᠡᠯᠡᠭᠡᠷ ᠰᠠᠢᠬᠠᠨ ᠨᠤᠲᠤᠭ᠎ᠢᠶᠠᠨ</p>
<p>ᠰᠡᠳᠭᠢᠯ ᠪᠠᠬᠠᠳᠤᠨ ᠬᠠᠷᠠᠬᠤ᠎ᠳᠤ</p>
<p>ᠦᠯᠢᠶᠡᠵᠦ ᠪᠠᠢᠭ᠎ᠠ ᠰᠠᠯᠬᠢ᠎ᠨᠢ</p>
<p>ᠦᠨᠦᠰᠦᠭᠡᠳ ᠪᠠᠢᠭ᠎ᠠ᠎ᠴᠤ᠎ᠶᠤᠮ᠎ᠰᠢᠭ᠋</p>
<p>ᠥᠷᠦᠰᠢᠶᠡᠯᠲᠦ ᠡᠵᠢ᠎ᠶᠢᠨ᠎ᠮᠢᠨᠢ ᠭᠠᠷ</p>
<p>ᠢᠯᠪᠢᠭᠡᠳ ᠪᠠᠢᠭ᠎ᠠ᠎ᠴᠤ᠎ᠶᠤᠮ᠎ᠰᠢᠭ᠋</p>
<p>ᠡᠨᠡᠷᠢᠩᠭᠦᠢ ᠰᠠᠢᠬᠠᠨ ᠰᠠᠨᠠᠭᠳᠠᠬᠤ᠎ᠳᠤ</p>
<p>ᠡᠯᠢᠭᠡ ᠵᠢᠷᠦᠬᠡ᠎ᠮᠢᠨᠢ ᠳᠣᠭᠳᠣᠯᠵᠣ</p>
<p>ᠬᠣᠣᠰ᠎ᠦᠭᠡᠢ ᠪᠠᠶᠠᠷ᠎ᠤᠨ ᠨᠢᠯᠪᠤᠰᠤ</p>
<p>ᠬᠣᠶᠠᠷ ᠨᠢᠳᠦ᠎ᠶᠢ᠎ᠮᠢᠨᠢ ᠪᠦᠷᠬᠦᠳᠡᠭ᠃</p>
<br/>
")

(def html (str "<html><head><meta name=\"viewport\" content=\"user-scalable=1.0,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0\">" css "</head><body>" content "</body></html>"))

(declare web-content)

(defn view []
  (let [webview-height (reagent/atom 0)
        on-message (fn [e]
                     (js/console.log  (j/get-in e [:nativeEvent :data]))
                     (reset! webview-height (j/get-in e [:nativeEvent :data])))]
    (fn []
     [nbase/box {:h "100%"}; :safeArea true}
      [nbase/measured-text {:fontFamily "MongolianBaiZheng"} "ᠴ᠂ ᠴᠢᠮᠡᠳ"]
      [nbase/scroll-view {:flex 1 :_contentContainerStyle {:flexGrow 1 :width @webview-height}
                          :horizontal true}
       ; [:> WebView {:source {:uri "https://reactnative.dev/"}}]]])
       [web-content html on-message]]])))

(defn web-content [html on-message]
  [:> WebView {:useWebKit true
               :cacheEnabled false
               :scrollEnabled false
               :scrollEventThrottle 10
               :hideKeyboardAccessoryView true
               :keyboardDisplayRequiresUserAction false
               :originWhitelist ["*"]
               :startInLoadingState true
               :bounces false
               :javaScriptEnabled true
               ; :automaticallyAdjustContentInsets false
               :source {:html html :baseUrl ""}
               :onMessage on-message
               :injectedJavaScript "window.ReactNativeWebView.postMessage(Math.max(document.body.offsetWidth, document.body.scrollWidth));"
               :style {:height "100%"
                       :width "100%"
                       :margin-bottom 10}}])

(defn web-view-message []
  (let [webref (reagent/atom nil)
        on-message (fn [data] (js/alert (j/get-in data [:nativeEvent :data])))]
    (fn []
      [nbase/box {:h "100%"};
       [nbase/center
        [nbase/pressable
         {:on-press (fn [e] (js/console.log "pressed ....")
                      (j/call @webref :postMessage "Data from React Native App"))
          :p 20 :width 300 :mt 100 :bg "#6751ff" :alignItems "center"}
         [nbase/text {:fontSize 20 :color "white"}
          "Send Data To WebView / Website"]]]
       [:> WebView
        {:ref (fn [r]
                (js/console.log ">>>> " r)
                (reset! webref r))
         :scalesPageToFit false
         :mixedContentMode "compatibility"
         :onMessage on-message
         :source { :html "
                          <html>
                          <head>
                            <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\" />
                          </head>
                          <body
                            style=\"
                              display: flex;
                              justify-content: center;
                              flex-direction: column;
                              align-items: center;
                            \"
                          >
                            <button
                            onclick=\"sendDataToReactNativeApp()\"
                              style=\"
                                padding: 20;
                                width: 200;
                                font-size: 20;
                                color: white;
                                background-color: #6751ff;
                              \"
                            >
                              Send Data To React Native App
                            </button>
                            <script>
                              const sendDataToReactNativeApp = async () => {
                                window.ReactNativeWebView.postMessage('Data from WebView / Website');
                              };
                              window.addEventListener(\"message\", message => {
                                alert(message.data)
                              });
                            </script>
                          </body>
                          </html>



                 "}}]])))

; (def quill-html (js/require "../assets/quill.html"))
(declare quill-html)

(def webref (reagent/atom nil))
(def cursor (reagent/atom nil))
(def is-caret (reagent/atom nil))


(defn webview-editor []
  (let [webview-width (reagent/atom 0)
        screen-width (.-width (.get Dimensions "window"))
        content (reagent/atom nil)
        scroll-position (reagent/atom 0)
        cursor-dot (reagent/atom false)
        cursor-dot-fn (Debouncer. (fn [] (reset! cursor-dot false)) 2000)
        cursor-dot-delay (reagent/atom false)
        cursor-dot-delay-fn (Debouncer. (fn [] (reset! cursor-dot-delay false)) 500)
        range (reagent/atom nil)
        is-menu (reagent/atom true)

        on-message (fn [e]
                     (let [data (js->clj (j/call js/JSON :parse (j/get-in e [:nativeEvent :data]))
                                   :keywordize-keys true)]
                       (condp = (:type data)
                         "initHeight" (do
                                        (js/console.log (bean/->js data))
                                        (reset! webview-width (max (:message data) screen-width)))
                         "onChange" (do
                                      (js/console.log (bean/->js data))
                                      (reset! content (:text (:message data)))
                                      (reset! webview-width (max (:width (:messge data)) screen-width)))
                         "updateSelection" (do (js/console.log (j/get-in e [:nativeEvent :data]))
                                               (if (not= 0 (-> data :message :left))
                                                 (reset! cursor (:message data)))
                                               (reset! is-caret true))
                         "copyText" (do (.setString Clipboard (:message data))
                                      (js/console.log (j/get-in e [:nativeEvent :data])))

                         "initRange" (do (js/console.log (j/get-in e [:nativeEvent :data])
                                           (reset! range (:message data)))
                                       (reset! is-caret false))
                         "updateRange" (do (js/console.log (j/get-in e [:nativeEvent :data]))
                                         (let [end-position (-> data :message :end)]
                                           (if (and (not= 0 (:left end-position)))
                                             (reset! range (:message data))))
                                         (reset! is-caret false)))))
        pan-start-location (reagent/atom nil)
        pan-translate (reagent/atom nil)
        options (.stringify js/JSON
                     (clj->js
                             {
                              :modules #js {:toolbar false}
                              :theme "snow"
                              :readOnly true}))]
                              ; :debug "info"
                              ; :placeholder "ᠠᠭᠤᠯᠭ᠎ᠠ ᠪᠠᠨ ᠨᠠᠢᠷᠠᠭᠤᠯᠤᠶ᠎ᠠ ..."}))]
    (fn []
      [gesture/gesture-root-view
       {:flex 1}
       [gesture/long-press-gesture-handler
        {:onHandlerStateChange (fn [e]
                                 (when (gesture/long-press-active (j/get e :nativeEvent))
                                   (js/console.log "long press >>>>>")
                                   (js/console.log (j/get e :nativeEvent))
                                   (j/call @webref :postMessage
                                     (j/call js/JSON :stringify (clj->js {:type "initRange" :message {:x (j/get-in e [:nativeEvent :x]), :y (j/get-in e [:nativeEvent :y])}})))))}


        [gesture/tap-gesture-handler
         {
           :onHandlerStateChange #(let [state (j/get-in % [:nativeEvent :state])]
                                    (cond
                                      (and (= 4 state) (true? @cursor-dot-delay))
                                      (do
                                        (js/console.log "cursor-dot delay run")
                                        (reset! cursor-dot true)
                                        (.fire cursor-dot-fn))

                                      (and (= 4 state) (false? @cursor-dot-delay))
                                      (do
                                        (js/console.log "cursor-dot delay prepare")
                                        (reset! cursor-dot-delay true)
                                        (.fire cursor-dot-delay-fn)))

                                    (js/console.log "tap gesture on scroll view" (j/get-in % [:nativeEvent :state]))
                                    (if (gesture/tap-state-end (j/get % :nativeEvent))
                                      (do
                                        (j/call @webref :postMessage
                                          (j/call js/JSON :stringify #js {:type "setSelection" :message #js {:x (+ (j/get-in % [:nativeEvent :x]) @scroll-position) :y (j/get-in % [:nativeEvent :y])}}))
                                        (js/console.log "tap gesture" (j/get % :nativeEvent)))))}
         [nbase/scroll-view {:flex 1 :_contentContainerStyle {:flexGrow 1 :width (+ 100 @webview-width)}
                             :horizontal true
                             :on-press (fn [e] (js/console.log "scroll-view on press"))
                             :scrollEventThrottle 16
                             :on-scroll (fn [e]
                                          (js/console.log "scroll-view-on-scroll")
                                          (js/console.log "on scroll >>>" (j/get-in e [:nativeEvent :contentOffset :x]))
                                          (reset! scroll-position (j/get-in e [:nativeEvent :contentOffset :x])))}
          ; [nbase/box {:style {:width @webview-width :height "100%"}}]
          [nbase/box {:style {:width (+ 100 @webview-width) :height "100%"}
                      :pointerEvents "none"}
           [:> WebView {:useWebKit true
                        :ref (fn [r] (reset! webref r))
                        :cacheEnabled false
                        :scrollEnabled false
                        :scrollEventThrottle 10
                        :hideKeyboardAccessoryView true
                        :keyboardDisplayRequiresUserAction false
                        :originWhitelist ["*"]
                        :startInLoadingState true
                        :bounces false
                        :javaScriptEnabled true
                        :source {:html html/quill-html
                                 :baseUrl ""}
                        :focusable false
                        :onMessage on-message
                        :injectedJavaScriptBeforeContentLoaded (str "window.options=" options)
                        :injectedJavaScript "
                                          _postMessage({type: 'initHeight', message: Math.max(document.body.offsetWidth, document.body.scrollWidth)});
                                          var length = quill.getLength();
                                          var range = pointFromSelection(length - 1);
                                          _postMessage({type: 'updateSelection', message: range});
                                          "
                        :style {:height "100%"
                                :width "100%"
                                :margin-bottom 10}
                        :pointerEvents "none"}]]
          (if (true? @is-caret)
            [nbase/box {:style {:top (:top @cursor) :left (:left @cursor)}
                        :position "absolute"
                        :flex-direction "row"}
             [:> blinkview {"useNativeDriver" false}
              [:> svg/Svg {:width 18 :height 2}
               [:> svg/Rect {:x "0" :y "0" :width 18 :height 2 :fill "blue"}]]]
             (if (true? @cursor-dot)
               [gesture/tap-gesture-handler
                {:onHandlerStateChange #(do
                                          (if (gesture/tap-state-end (j/get % :nativeEvent))
                                            (do
                                              (js/console.log "caret dot tap gesture" (j/get % :nativeEvent)))))}
                [gesture/pan-gesture-handler
                 {:onGestureEvent
                  (fn [e] (let [x (+ (:left @pan-start-location) (j/get-in e [:nativeEvent :translationX]))
                                ; y (j/get-in e [:nativeEvent :translationY])
                                y (+ (:top @pan-start-location) (j/get-in e [:nativeEvent :translationY]))]
                            ; (js/console.log (j/get e :nativeEvent))
                            (js/console.log "x11 = " x " y = " y)
                            (.fire cursor-dot-fn)
                            (j/call @webref :postMessage
                              (j/call js/JSON :stringify #js {:type "setSelection" :message #js {:x x :y y}}))))
                  :onHandlerStateChange
                  (fn [e] (when (= 2 (j/get-in e [:nativeEvent :state]))
                            (js/console.log "caret dot set pan start location!!!!" (bean/->js @cursor))
                            (reset! pan-start-location @cursor)))}

                 [nbase/box {:style {:margin-top -9
                                     :margin-left 5}}
                  [nbase/box {
                              :w 5
                              :h 5
                              :border-top-radius "full"
                              :border-bottom-right-radius "full"
                              :bg "blue.600"
                              :style
                              {
                               :transform [{:rotate "45deg"}]}}]]]])])

          (if (false? @is-caret)
             [nbase/box {:style {:top (- (:top (:start @range)) 20) :left (+ (:left (:start @range)) 20)}
                         :position "absolute"}
              [gesture/pan-gesture-handler
               {:onGestureEvent
                (fn [e] (let [x (+ (:left @pan-start-location) (j/get-in e [:nativeEvent :translationX]))
                              y (+ (:top @pan-start-location) (j/get-in e [:nativeEvent :translationY]))]
                          (js/console.log "x = " x " y = " y)
                          (j/call @webref :postMessage
                            (j/call js/JSON :stringify (clj->js {:type "updateRange"
                                                                 :message
                                                                 {:start {:x x :y y}
                                                                  :end {:x (:left (:end @range))
                                                                        :y (:top (:end @range))}}})))))
                :onHandlerStateChange
                (fn [e]
                  (condp = (j/get-in e [:nativeEvent :state])
                    2 (do
                        (reset! is-menu false)
                        (reset! pan-start-location (:start @range)))
                    5 (do (reset! is-menu true))
                    (js/console.log "xxx " (j/get-in e [:nativeEvent :state]))))}
               [nbase/box {
                           :w 5
                           :h 5
                           :border-top-radius "full"
                           :border-bottom-right-radius "full"
                           :bg "blue.600"}]]])
          (if (false? @is-caret)
             [nbase/box {:style {:top (:top (:end @range))
                                 :left (+ (:left (:end @range)) 20)}
                         :position "absolute"}
              [gesture/tap-gesture-handler
               {:onHandlerStateChange #(do
                                         (if (gesture/tap-state-end (j/get % :nativeEvent))
                                           (do
                                             (js/console.log "range end tap gesture" (j/get % :nativeEvent)))))}
               [gesture/pan-gesture-handler
                {:onGestureEvent
                 (fn [e] (let [x (+ (:left @pan-start-location) (j/get-in e [:nativeEvent :translationX]))
                               y (+ (:top @pan-start-location) (j/get-in e [:nativeEvent :translationY]))]
                           (js/console.log "range end x = " x " y = " y)
                           (j/call @webref :postMessage
                             (j/call js/JSON :stringify (clj->js {:type "updateRange"
                                                                  :message
                                                                  {:start {:x (:left (:start @range))
                                                                           :y (:top (:start @range))}
                                                                   :end {:x x :y y}}})))))
                 :onHandlerStateChange
                 (fn [e]
                   (condp = (j/get-in e [:nativeEvent :state])
                     2 (do
                         (reset! is-menu false)
                         (reset! pan-start-location (:end @range)))
                     5 (do (reset! is-menu true))
                     (js/console.log "xxx " (j/get-in e [:nativeEvent :state]))))}
                [nbase/box {:w 5
                            :h 5
                            :border-bottom-radius "full"
                            :border-top-right-radius "full"
                            :bg "blue.600"}]]]])
          (if (false? @is-caret)
            (for [x (:ranges @range)]
              ^{:key (str "range-area-" (:top x) "-" (:left x))}
              [nbase/box {:style {:width (:width x)
                                  :height (:height x)
                                  :left (:left x)
                                  :top (:top x)}
                          :position "absolute"
                          :bg "blue.600:alpha.30"}]))
          (if (and (false? @is-caret) (true? @is-menu))
            (let [screen-width (.-width (.get Dimensions "window"))
                  end-left (- (:left (:end @range)) @scroll-position)
                  start-left (- (:left (:start @range)) @scroll-position)
                  left (cond
                         (and (neg? end-left) (neg? start-left))
                         @scroll-position

                         (and (> end-left screen-width) (> start-left screen-width))
                         @scroll-position

                         :else
                         (+ @scroll-position end-left 60))]

             [nbase/box {:style {:left left
                                 :top 20
                                 :padding 12}
                         :position "absolute"
                         :flex-grow 1
                         :flex-shrink 1
                         :flex-direction "column"
                         :shadow "9"
                         :bg "lightText"
                         :border-radius "md"
                         :justifyContent "flex-end"
                         :elevation 3001}
                         ; :w 10
              [gesture/tap-gesture-handler
               {:onHandlerStateChange
                (fn [e]
                  (when (gesture/tap-state-end (j/get e :nativeEvent))
                    (js/console.log "paste")
                    (.then (.getString Clipboard)
                      (fn [x]
                        (j/call @webref :postMessage
                          (j/call js/JSON :stringify
                            (bean/->js {:type "insertText" :message {:index (:index (:start @range))
                                                                     :text x}})))))))}
               [nbase/pressable
                [nbase/measured-text {} "ᠨᠠᠭᠠᠬᠤ"]]]
              [nbase/divider {:my 2}]
              [gesture/tap-gesture-handler
               {:onHandlerStateChange
                (fn [e]
                  (when (gesture/tap-state-end (j/get e :nativeEvent))
                    (js/console.log "copy")
                    (j/call @webref :postMessage
                      (j/call js/JSON :stringify
                        (bean/->js {:type "copyText"
                                    :message {:start (:index (:start @range))
                                              :end (:index (:end @range))}})))))}
               [nbase/pressable
                [nbase/measured-text {} "ᠬᠠᠭᠤᠯᠬᠤ"]]]
              [nbase/divider {:my 2}]
              [gesture/tap-gesture-handler
               {:onHandlerStateChange
                (fn [e]
                  (when (gesture/tap-state-end (j/get e :nativeEvent))
                    (js/console.log "delete")
                    (j/call @webref :postMessage
                      (j/call js/JSON :stringify
                        (bean/->js {:type "deleteText"
                                    :message {:start (:index (:start @range))
                                              :end (:index (:end @range))}})))))}
               [nbase/pressable
                [nbase/measured-text {} "ᠬᠠᠰᠤᠬᠤ"]]]
              [nbase/divider {:my 2}]
              [gesture/tap-gesture-handler
               {:onHandlerStateChange
                (fn [e]
                  (when (gesture/tap-state-end (j/get e :nativeEvent))
                    (j/call @webref :postMessage
                      (j/call js/JSON :stringify
                        (bean/->js {:type "selectAll"
                                    :message ""})))))}
               [nbase/pressable
                [nbase/measured-text {} "ᠪᠦᠭᠦᠳᠡ"]]]]))]]]])))

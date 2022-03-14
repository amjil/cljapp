(ns app.ui.webview
  (:require
   [app.ui.nativebase :as nbase]
   [app.ui.components :as ui]
   [app.ui.html :as html]
   [app.handler.gesture :as gesture]
   [reagent.core :as reagent]
   [applied-science.js-interop :as j]
   ["react-native" :as rn]
   ["react-native-webview" :refer [WebView]]
   ["react-native-smooth-blink-view" :default blinkview]
   ["react-native-svg" :as svg]))

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

(defn webview-editor []
  (let [webview-height (reagent/atom 0)
        webref (reagent/atom nil)
        content (reagent/atom nil)
        cursor (reagent/atom nil)
        is-caret (reagent/atom true)
        on-message (fn [e]
                     (let [data (js->clj (j/call js/JSON :parse (j/get-in e [:nativeEvent :data]))
                                   :keywordize-keys true)]
                       (condp = (:type data)
                         "initHeight" (reset! webview-height (:message data))
                         "onChange" (reset! content (:message data))
                         "updateSelection" (do (js/console.log (j/get-in e [:nativeEvent :data]))
                                               (reset! cursor (:message data))
                                               (reset! is-caret true))

                         "initRange" (do (js/console.log (j/get-in e [:nativeEvent :data]))
                                       (reset! range (:message data))
                                       (reset! is-caret false))
                         "updateRange" (do (js/console.log (j/get-in e [:nativeEvent :data]))
                                         (reset! range (:message data))
                                         (reset! is-caret false)))))
        options (.stringify js/JSON
                     (clj->js
                             {
                              :modules #js {:toolbar false}
                              :theme "snow"
                              :readOnly true}))]
                              ; :debug "info"
                              ; :placeholder "ᠠᠭᠤᠯᠭ᠎ᠠ ᠪᠠᠨ ᠨᠠᠢᠷᠠᠭᠤᠯᠤᠶ᠎ᠠ ..."}))]
    (fn []
      [nbase/box {:h "100%"}
       [nbase/pressable
        {:on-press (fn [e]
                     (j/call @webref :postMessage
                       ; (j/call js/JSON :stringify #js {:type "testMessage" :message "Data from React Native App"})
                       (j/call js/JSON :stringify #js {:type "setSelection" :message #js {:x 28, :y 57}}))
                       ; (j/call js/JSON :stringify #js {:type "insertText" :message #js {:index 5, :text " new text"}}))
                       ; (j/call js/JSON :stringify #js {:type "initRange" :message #js {:x 30, :y 20}})
                       ; (j/call js/JSON :stringify #js {:type "updateRange" :message (clj->js {:start {:x 17, :y 13} :end {:x 17, :y 50}})})))}
                     (js/console.log "pressed ...."))}
        [nbase/text
         "button"]]
       [nbase/scroll-view {:flex 1 :_contentContainerStyle {:flexGrow 1 :width @webview-height}
                           :horizontal true
                           :on-press #(js/console.log "scroll-view on press")}
        [nbase/zstack {:width "100%" :height "100%"}
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
                      :onMessage on-message
                      :injectedJavaScriptBeforeContentLoaded (str "window.options=" options)
                      :injectedJavaScript "
                                          //window.ReactNativeWebView.postMessage(Math.max(document.body.offsetWidth, document.body.scrollWidth));


                                          _postMessage({type: 'initHeight', message: Math.max(document.body.offsetWidth, document.body.scrollWidth)})
                                          "
                      :style {:height "100%"
                              :width "100%"
                              :margin-bottom 10}}]
         [nbase/box {:style {:margin-top (:top @cursor) :margin-left (:left @cursor)}
                     :flex-direction "row"}
          [:> blinkview {"useNativeDriver" false}
           [:> svg/Svg {:width 18 :height 2}
            [:> svg/Rect {:x "0" :y "0" :width 18 :height 2 :fill "blue"}]]]
          [gesture/tap-gesture-handler
           {:onHandlerStateChange #(do
                                     (if (gesture/tap-state-end (j/get % :nativeEvent))
                                       (js/console.log "tap gesture")))}
           [gesture/pan-gesture-handler
            {:onGestureEvent
             (fn [e] (let [x (+ (j/get-in e [:nativeEvent :translationX]) 15)
                           y (j/get-in e [:nativeEvent :translationY])]
                       (js/console.log "x = " x " y = " y)
                       (j/call @webref :postMessage
                         (j/call js/JSON :stringify #js {:type "setSelection" :message #js {:x x :y y}}))))}
            [nbase/box {:flex-direction "row" :mt -2}
             [nbase/box {:w 0 :h 0
                         :mr -2
                         :border-right-width 15
                         :border-right-color "blue.600"
                         :border-top-color "transparent"
                         :border-top-width 10
                         :border-bottom-color "transparent"
                         :border-bottom-width 10
                         :border-left-color "transparent"
                         :border-left-width 0}]
             [nbase/box {:w 5 :h 5 :border-radius 50 :bg "blue.600"}]]]]]]]])))

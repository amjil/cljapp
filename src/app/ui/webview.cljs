(ns app.ui.webview
  (:require
   [app.ui.nativebase :as nbase]
   [app.ui.components :as ui]
   [reagent.core :as reagent]
   [applied-science.js-interop :as j]
   ["react-native" :as rn]
   ["react-native-webview" :refer [WebView]]))

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
<p>&nbsp;</p>
<p>ᠥᠷᠭᠡᠨ ᠰᠠᠢᠬᠠᠨ ᠨᠤᠲᠤᠭ᠎ᠤᠨ᠎ᠢᠶᠠᠨ</p>
<p>ᠡᠪᠴᠢᠭᠦᠨ ᠳᠡᠭᠡᠷ᠎ᠡ᠎ᠨᠢ ᠲᠥᠷᠥᠭᠡᠳ</p>
<p>ᠥᠨᠢᠷ ᠠᠭᠤᠳᠠᠮ ᠲᠠᠯ᠎ᠠ᠎ᠶᠢᠨ᠎ᠢᠶᠠᠨ</p>
<p>ᠥᠩᠭᠡᠲᠦ ᠠᠮᠢᠳᠤᠷᠠᠯ᠎ᠤᠨ ᠲᠥᠯᠦᠭᠡ</p>
<p>ᠠᠮᠢ ᠪᠡᠶ᠎ᠡ᠎ᠪᠡᠨ ᠵᠣᠷᠢᠭᠤᠯᠳᠠᠭ</p>
<p>ᠠᠷᠢᠭᠤᠨ ᠨᠠᠨᠳᠢᠨ ᠵᠠᠩᠰᠢᠯ᠎ᠲᠠᠢ</p>
<p>ᠡᠪᠦᠭᠡᠳ ᠬᠥᠭᠰᠢᠳ ᠳᠡᠭᠡᠳᠦᠰ᠎ᠦᠨ᠎ᠮᠢᠨᠢ</p>
<p>ᠦᠷ᠎ᠡ᠎ᠪᠡᠨ ᠭᠡᠵᠦ ᠨᠠᠢᠳᠠᠭᠠᠳ</p>
<p>ᠦᠯᠡᠳᠡᠭᠡᠵᠦ ᠨᠠᠳᠠ᠎ᠳᠤ ᠥᠭᠭᠦᠭᠰᠡᠨ</p>
<p>ᠥᠨᠢᠷ ᠮᠣᠩᠭᠤᠯ ᠣᠷᠤᠨ᠎ᠤ</p>
<p>ᠨᠠᠷᠠ᠎ᠨᠢ ᠨᠠᠮᠠᠢ ᠭᠡᠢᠭᠦᠯᠵᠦ</p>
<p>ᠨᠠᠷᠢᠨ ᠰᠠᠯᠬᠢ᠎ᠨᠢ ᠦᠯᠢᠶᠡᠵᠦ</p>
<p>ᠨᠠᠮᠢᠷᠠᠭ᠎ᠠ ᠬᠤᠷ᠎ᠠ᠎ᠨᠢ ᠲᠡᠵᠢᠭᠡᠵᠦ</p>
<p>ᠨᠠᠢᠷᠲᠠᠢ ᠰᠠᠢᠬᠠᠨ ᠣᠷᠤᠨ᠎ᠮᠢᠨᠢ</p>
<p>ᠨᠠᠳᠠ᠎ᠲᠠᠢ ᠬᠤᠪᠢ ᠨᠢᠭᠡᠲᠡᠢ ᠪᠠᠢᠳᠠᠭ᠎ᠲᠤᠯᠠ</p>
<p>ᠤᠨᠠᠭᠰᠠᠨ ᠡᠨᠡ ᠨᠤᠲᠤᠭ᠎ᠢᠶᠠᠨ ᠪᠢ</p>
<p>ᠥᠪᠡᠷ᠎ᠦᠨ ᠪᠡᠶ᠎ᠡ᠎ᠰᠢᠭ ᠬᠠᠢᠷᠠᠯᠠᠵᠤ</p>
<p>ᠤᠭᠢᠶᠠᠭᠰᠠᠨ ᠲᠤᠩᠭᠠᠯᠠᠭ ᠮᠥᠷᠡᠨ᠎ᠢᠶᠡᠨ ᠪᠢ</p>
<p>ᠡᠬᠡ᠎ᠶᠢᠨ ᠰᠦ᠋᠎ᠰᠢᠭ ᠰᠠᠨᠠᠳᠠᠭ᠃</p>
<p>&nbsp;</p>
<p>ᠡᠨᠡ ᠨᠤᠲᠤᠭ᠎ᠲᠠᠭᠠᠨ ᠪᠢ ᠡᠵᠡᠨ᠎ᠨᠢ᠎ᠶᠦᠮ</p>
<p>ᠡᠩᠬᠦᠷᠡᠢᠯᠡᠯ᠎ᠳᠦ᠎ᠨᠢ ᠥᠰᠦᠭᠰᠡᠨ ᠬᠦᠦ᠎ᠨᠢ᠎ᠶᠦᠮ</p>
<p>ᠢᠷᠡᠭᠡᠳᠦᠢ᠎ᠶᠢ᠎ᠨᠢ ᠲᠦᠮᠡᠨ᠎ᠲᠡᠢ᠎ᠪᠡᠨ ᠪᠣᠰᠭᠠᠯᠴᠠᠬᠤ</p>
<p>ᠶᠡᠬᠡ ᠡᠭᠦᠷᠭᠡ ᠬᠦᠯᠢᠶᠡᠭᠰᠡᠨ᠎ᠶᠦᠮ᠃</p>
<p>&nbsp;</p>
<p>ᠬᠡᠷᠪᠡ ᠬᠥᠮᠦᠨ ᠡᠭᠡ ᠣᠷᠤᠨ᠎ᠢᠶᠠᠨ</p>
<p>ᠬᠠᠮᠤᠭ᠎ᠠᠴᠠ ᠦᠨᠡᠲᠡᠢ ᠭᠡᠳᠡᠭ ᠪᠣᠯ</p>
<p>ᠬᠡᠭᠡᠷ᠎ᠡ ᠲᠠᠯ᠎ᠠ ᠨᠤᠲᠤᠭ᠎ᠢᠶᠠᠨ</p>
<p>ᠶᠠᠭᠤ᠎ᠠᠴᠠ᠎ᠴᠤ ᠢᠯᠡᠭᠦᠦ ᠰᠠᠨᠠᠳᠠᠭ ᠪᠣᠯ</p>
<p>ᠠᠵᠢᠯᠯᠠᠬᠤ ᠬᠥᠳᠡᠯᠬᠦ ᠪᠦᠷᠢ᠎ᠳᠡᠭᠡᠨ</p>
<p>ᠠᠷᠠᠳ ᠲᠦᠮᠡᠨ᠎ᠦ᠎ᠪᠡᠨ ᠲᠥᠯᠦᠭᠡ ᠪᠣᠯ</p>
<p>ᠠᠯᠬᠤᠬᠤ ᠭᠢᠰᠭᠢᠬᠦ ᠪᠣᠯᠭᠠᠨ᠎ᠳᠠᠭᠠᠨ</p>
<p>ᠠᠤᠭ᠎ᠠ ᠣᠷᠤᠨ᠎ᠤ᠎ᠪᠠᠨ ᠲᠥᠯᠦᠭᠡ ᠪᠣᠯ</p>
<p>ᠠᠯᠲᠠᠨ ᠰᠢᠷᠤᠢ ᠤᠷᠤᠭᠤ᠎ᠪᠠᠨ ᠳᠠᠭᠠᠷᠢᠭᠰᠠᠨ</p>
<p>ᠠᠲᠠᠭᠠᠲᠠᠨ᠎ᠤ ᠬᠣᠣᠷᠲᠤ ᠰᠤᠮᠤ᠎ᠶᠢ</p>
<p>ᠠᠶᠤᠰᠢ᠎ᠰᠢᠭ᠋ ᠴᠡᠭᠡᠵᠢ᠎ᠪᠡᠷ᠎ᠢᠶᠡᠨ ᠲᠠᠭᠯᠠᠨ</p>
<p>ᠠᠮᠢ᠎ᠪᠠᠨ ᠥᠭᠬᠦ᠎ᠡᠴᠡ ᠪᠤᠴᠠᠬᠤ᠎ᠦᠭᠡᠢ ᠪᠣᠯ</p>
<p>ᠡᠨᠡ ᠬᠥᠮᠦᠨ ᠮᠣᠩᠭᠤᠯ ᠬᠥᠮᠦᠨ</p>
<p>ᠡᠬᠡ ᠣᠷᠤᠨ᠎ᠳᠠᠭᠠᠨ ᠬᠠᠢᠷᠠᠲᠠᠢ ᠬᠥᠮᠦᠨ</p>
<p>ᠡᠨᠡ ᠬᠠᠢᠷ᠎ᠠ᠎ᠨᠢ ᠮᠣᠩᠭᠤᠯᠴᠤᠳ᠎ᠲᠤ</p>
<p>ᠡᠷᠲᠡᠨ᠎ᠡᠴᠡ ᠬᠡᠪᠰᠢᠭᠰᠡᠨ ᠬᠠᠢᠷ᠎ᠠ ᠭᠡᠨ᠎ᠡ᠃</p>
<p>&nbsp;</p>
<p>ᠪᠢ ᠮᠣᠩᠭᠤᠯ ᠬᠥᠮᠦᠨ ᠪᠣᠯᠬᠣᠷ</p>
<p>ᠪᠢᠳᠤᠷᠶ᠎ᠠ ᠨᠤᠲᠤᠭ᠎ᠤᠨ᠎ᠢᠶᠠᠨ ᠲᠥᠯᠦᠭᠡ</p>
<p>ᠠᠮᠢ ᠪᠡᠶ᠎ᠡ᠂ ᠪᠦᠬᠦ ᠨᠠᠰᠤ᠎ᠪᠠᠨ</p>
<p>ᠠᠪᠴᠤ ᠪᠠᠢᠭ᠎ᠠ ᠠᠮᠢᠰᠭᠤᠭ᠎ᠠ᠎ᠪᠠᠨ</p>
<p>ᠣᠷᠭᠢᠯᠵᠤ ᠪᠠᠢᠭ᠎ᠠ ᠵᠣᠷᠢᠭ᠎ᠢᠶᠠᠨ</p>
<p>ᠠᠯᠢ᠎ᠶᠢ᠎ᠨᠢ᠎ᠴᠤ ᠬᠠᠢᠷᠠᠯᠠᠯ᠎ᠦᠭᠡᠢ ᠵᠣᠷᠢᠭᠤᠯᠤᠨ᠎ᠠ᠃</p>
<p>&nbsp;</p>
<p>ᠲᠥᠷᠦᠯᠬᠢ ᠨᠤᠲᠤᠭ᠎ᠢᠶᠠᠨ ᠭᠡᠢᠭᠦᠯᠦᠭᠰᠡᠨ</p>
<p>ᠨᠠᠷᠠᠨ᠎ᠤ ᠲᠤᠶᠠᠭᠠᠨ᠎ᠳᠤ ᠬᠠᠢᠷᠠᠲᠠᠢ ᠪᠢ</p>
<p>ᠲᠦᠪᠰᠢᠨ ᠲᠠᠯ᠎ᠠ᠎ᠳᠠᠭᠠᠨ ᠴᠤᠤᠷᠢᠶᠠᠲᠤᠭᠰᠠᠨ</p>
<p>ᠡᠭᠡᠰᠢᠭᠲᠦ ᠲᠠᠭᠤᠨ᠎ᠳᠤ ᠳᠤᠷᠠᠲᠠᠢ ᠪᠢ</p>
<p>ᠬᠠᠨᠠᠭᠠᠷ ᠴᠢᠯᠦᠭᠡᠲᠦ ᠨᠤᠲᠤᠭ᠎ᠢᠶᠠᠨ</p>
<p>ᠬᠠᠷᠠᠬᠤ ᠳᠣᠲᠣᠮ᠎ᠢᠶᠠᠨ ᠪᠠᠶᠠᠰᠴᠤ ᠪᠠᠢᠨ᠎ᠠ</p>
<p>ᠬᠠᠯᠠᠭᠤᠨ ᠠᠮᠢ᠎ᠪᠠᠨ ᠲᠦᠢᠬᠦ᠎ᠦᠭᠡᠢ</p>
<p>ᠵᠢᠳᠭᠦᠬᠦ ᠵᠣᠷᠢᠭ ᠨᠡᠮᠡᠭᠳᠡᠵᠦ ᠪᠠᠢᠨ᠎ᠠ᠃</p>

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

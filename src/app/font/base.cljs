(ns app.font.base
  (:require
   ["@pdf-lib/fontkit" :as fontkit]
   ["react-native-fs" :as fs]
   ["react-native" :as rn]
   [goog.crypt.base64 :as b64]
   [applied-science.js-interop :as j]
   [cljs-bean.core :refer [bean ->clj ->js]]
   [promesa.core :as p]
   [promesa.async-cljs :refer-macros [async]]
   [reagent.core :as reagent]))

;; (defonce ^:private
(def
  fonts
  (reagent/atom {}))

(defn init []
  (p/alet [result (p/await
                    (let [platform (j/get-in rn [:Platform :OS])]
                      (condp = platform
                        "android" (.readFileAssets fs "mnglwhiteotf.ttf" "base64")
                        "ios" (.readFile fs (str fs/MainBundlePath "/assets/monbaiti.ttf") "base64"))))]
    (swap! fonts assoc :white (fontkit/create (b64/decodeStringToUint8Array result))))
    ; (js/console.log "aaa"))
    ; (js/console.log (b64/decodeStringToUint8Array result)))
  ;; (async (p/await (p/delay 100)))
  (js/console.log "init function"))


(defn load [name url]
  (-> (fs/readFileAssets url)
      (.then (fn [res] (swap! fonts assoc name (fontkit/create res))))
      ;; (.then (fn [res] (swap! fonts assoc name res)))
      (.catch (fn [err] (js/console.log err)))))

;; (.then (.readFileAssets fs "monbaiti.ttf") (fn [res] (swap! fonts assoc :white (fontkit/create res))))
;; (.then (.readFileAssets fs "monbaiti.ttf" "base64") (fn [res] (swap! fonts assoc :white (fontkit/create (b64/decodeStringToUint8Array res)))))

(defn get-font [name]
  (get @fonts name))

(defn character-set [name]
  (j/get (get-font name) "characterSet"))

(defn font-size [font size]
  (/ size (j/get font :unitsPerEm)))

(defn svg [glyph size]
  (if (nil? (j/get glyph :path))
    ""
    (-> glyph
        (j/get :path)
        (j/call :scale -1 1)
        (j/call :rotate (str (.-PI js/Math)))
        (j/call :scale size)
        (j/call :toSVG))))

(defn space-dimention [font-name size]
  (let [font (get-font font-name)]
    (if font
      (let [glyph (-> font
                      (j/call :layout " ")
                      (j/get :glyphs)
                      last)
            inner-size (font-size font size)]
        [(* inner-size (j/get glyph :advanceWidth))
         (* inner-size (j/get glyph :advanceHeight))]))))

(defn run [font-name size word]
  (let [font (get-font font-name)]
    (if font
      (let [inner-size (font-size font size)
            glyphs     (-> font
                           (j/call :layout word)
                           (j/get :glyphs)
                           (->clj))]
        (map #(hash-map :svg (svg % inner-size)
                        :width (* inner-size (j/get % :advanceWidth))
                        :code-points (->clj (j/get % :codePoints)))
             glyphs)))))



(def mstr "ᠡᠷᠬᠡ")
(def mlongstr "ᠴᠠᠰᠤᠲᠤ ᠬᠠᠢᠷᠬᠠᠨ ᠬᠥᠪᠴᠢ ᠬᠠᠩᠭᠠᠢ ᠡᠯᠡᠰᠦᠨ ᠮᠠᠩᠬ᠎ᠠ ᠨᠢ ᠴᠠᠭ ᠊ᠤᠨ ᠤᠷᠲᠤ ᠳᠤ ᠬᠤᠪᠢᠷᠠᠭ᠎ᠠ ᠦᠭᠡᠢ ᠡᠭᠡᠯ ᠲᠥᠷᠬᠦ ᠲᠠᠢ ᠠᠭᠤᠯᠠ ᠤᠰᠤ ᠤᠷᠭᠤᠮᠠᠯ ᠠᠮᠢᠲᠠᠨ ᠪᠤᠭᠤᠷᠤᠯ ᠳᠡᠭᠡᠳᠦᠰ ᠮᠢᠨᠢ")
;; (def mlongstr "ᠴᠠᠰᠤᠲᠤ ᠬᠠᠷᠠᠬᠠᠨ ")

(comment
  (init)
  (run :white 24 "ᠠᠭᠤᠯᠠ ")
  (space-dimention :white 24)
  (font-size (get-font :white) 24)
  (-> (get-font :white)
      (j/call :layout " ")
      (j/get :glyphs)
      first
      (js/console.log))

  (map #(hash-map
        ;;  :svg (svg % 0.01171875)
         :height (* 0.01171875 (j/get % :advanceHeight))
                 :width (* 0.01171875 (j/get % :advanceWidth)))
                        ;; :code-points (j/get % :codePoints)

        (-> (get-font :white)
            (j/call :layout mstr)
            (j/get :glyphs)
            (->clj)))

  fonts
  (time (get-font :whtie))
  (time (get-glyphs :white mstr))
  (time (* 100 12 13))
  (-> (get-glyphs :white mstr)
      last)

  (time
   (-> (get-glyphs :white mstr)
       last
       (j/get :path)
       (j/call :scale 1 -1)
       (j/call :rotate (str (* 1 (.-PI js/Math))))
       (j/call :scale (* 0.12 (font-scale :white)))
       (j/call :toSVG)))


  (time
   (map #(width :white 24 %) (get-glyphs :white mstr)))
  (map #(svg % :white 24) (get-glyphs :white mstr))
  (map #(svg % :white 24) (get-glyphs :white mlongstr))

  mlongstr
  mstr
  (require '[clojure.string :as str])
  (time
   (->>
    (str/split mlongstr #" ")
    (map #(get-glyphs :white %))
    (map (fn [x] (map #(width :white 24 %) x)))))
  
  )

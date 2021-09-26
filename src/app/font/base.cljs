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
                        "android" (.readFileAssets fs "monbaiti.ttf" "base64")
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
(def mlongstr "ᠨᠠᠢᠵᠠ᠎ᠮᠢᠨᠢ᠂ ᠲᠠ ᠲᠡᠭᠷᠢ ᠠᠭᠤᠯᠠ᠎ᠳᠤ ᠬᠦᠷᠴᠦ ᠦᠵᠡᠪᠡ᠎ᠦᠦ? ᠲᠡᠭᠷᠢ ᠠᠭᠤᠯᠠ ᠪᠣᠯ ᠮᠠᠨ᠎ᠤ ᠤᠯᠤᠰ᠎ᠤᠨ ᠪᠠᠷᠠᠭᠤᠨ ᠬᠣᠢᠲᠤ ᠬᠢᠯᠢ ᠳᠠᠭᠠᠤ ᠬᠡᠳᠦᠨ ᠮᠢᠩᠭᠠᠨ ᠭᠠᠵᠠᠷ ᠦᠷᠭᠦᠯᠵᠢᠯᠡᠭᠰᠡᠨ ᠲᠣᠮᠤᠬᠠᠨ ᠨᠢᠷᠤᠭᠤ ᠮᠥᠨ ᠪᠥᠭᠡᠳ ᠵᠦᠩᠭᠠᠷ ᠲᠠᠷᠢᠮ ᠬᠣᠶᠠᠷ ᠬᠣᠲᠤᠭᠤᠷ᠎ᠤᠨ ᠳᠤᠮᠳᠠᠭᠤᠷ ᠬᠥᠨᠳᠡᠯᠢᠳᠴᠦ᠂ ᠥᠷᠭᠡᠨ ᠠᠭᠤᠳᠠᠮ ᠰᠢᠨᠵᠢᠶᠠᠩ ᠣᠷᠤᠨ᠎ᠢ ᠡᠮᠦᠨ᠎ᠡ ᠬᠣᠢᠨ᠎ᠠ ᠬᠣᠶᠠᠷ ᠬᠡᠰᠡᠭ ᠪᠣᠯᠭᠠᠨ ᠬᠤᠪᠢᠶᠠᠵᠠᠢ᠃ ᠲᠡᠭᠷᠢ ᠠᠭᠤᠯᠠ᠎ᠶᠢ ᠠᠯᠤᠰ᠎ᠠᠴᠠ ᠪᠠᠷᠠᠯᠠᠬᠤ᠎ᠳᠤ ᠲᠦᠮᠡᠨ ᠦᠵᠡᠮᠵᠢ ᠲᠡᠭᠦᠰ ᠪᠦᠷᠢᠳᠦᠭᠡᠳ ᠡᠭᠦᠯᠡᠨ ᠰᠠᠴᠤᠭ᠎ᠲᠤ ᠰᠢᠷᠭᠤᠭᠰᠠᠨ ᠮᠥᠩᠬᠡ ᠴᠠᠰᠤᠲᠤ ᠰᠠᠷᠢᠳᠠᠭ ᠣᠷᠭᠢᠯ᠎ᠤᠳ᠎ᠨᠢ ᠡᠭᠡᠭᠡ᠎ᠯᠡ ᠪᠥᠵᠢᠭᠯᠡᠯᠴᠡᠵᠦ ᠪᠠᠢᠭ᠎ᠠ ᠤᠢᠭᠤᠷ ᠬᠡᠦᠬᠡᠨᠴᠦᠦᠯ᠎ᠦᠨ ᠣᠷᠤᠢ᠎ᠶᠢᠨ ᠴᠢᠮᠡᠭ ᠪᠣᠯᠤᠭᠰᠠᠨ ᠲᠠᠨ᠎ᠠ ᠰᠤᠪᠤᠳ ᠰᠢᠭᠢᠳᠭᠡᠭᠡᠲᠦ ᠲᠢᠲᠢᠮ ᠠᠳᠠᠯᠢ ᠭᠢᠯᠪᠠᠯᠵᠠᠨ ᠭᠢᠯᠲᠦᠭᠡᠨᠡᠨ᠎ᠡ᠃ ᠠᠯᠠᠭᠯᠠᠨ ᠬᠠᠷᠠᠭᠳᠠᠬᠤ ᠥᠨᠳᠦᠷ ᠨᠠᠮ ᠨᠢᠷᠤᠭᠤ᠎ᠨᠤᠭᠤᠳ᠎ᠨᠢ ᠥᠳᠦ ᠰᠣᠳᠤ᠎ᠪᠠᠨ ᠰᠠᠭᠠᠳᠠᠭᠯᠠᠭᠳᠠᠭᠰᠠᠨ ᠲᠣᠭᠤᠰ ᠰᠢᠪᠠᠭᠤ ᠮᠡᠲᠦ ᠳᠤᠷ᠎ᠠ ᠪᠠᠬᠠᠷᠬᠠᠯ᠎ᠢ ᠲᠠᠲᠠᠭᠰᠠᠨ ᠪᠠᠢᠨ᠎ᠠ᠃")

(comment
  (init)
  (run :white 24 mstr)
  (font-size (get-font :white) 24)
  (-> (get-font :white)
      (j/call :layout mstr)
      (j/get :glyphs)
      (->clj))

  (map #(hash-map :svg (svg % 0.01171875)
                       :width (* 0.01171875 (j/get % :advanceWidth))
                        :code-points (j/get % :codePoints)))
            ;;  (-> (get-font :white)
            ;;                (j/call :layout mstr)
            ;;                (j/get :glyphs)
            ;;                (->clj))

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
  (-> (get-glyphs :white mstr)
      first
      (svg 24))

  (msvg)
  (load :white "./assets/fonts/monbaiti.ttf")
  load

  fs/readFile
  (get-font :white)
  (:white @fonts)

  (units-per-em :white)
  (font-scale :white)
  (time
   (units-per-em :white))

  ;; (js/console.log (layout :white "ᠡᠷᠬᠡ"))
  ;; (def glyphs (.-glyphs (layout :white "ᠡᠷᠬᠡ ")))
  ;; (def glyphs (.-glyphs (layout :white "aa")))
  glyphs

  (width :white 48 (first glyphs))

  (.-head (get-font :white))




  (require '[clj-bean.core :refer [bean ->clj ->js]])
  (def glyph-run (layout :white mstr))
  (-> (j/get glyph-run :glyphs)
      first)
  (def (-> (get-glyphs :white mstr)
           first)
    aa)
  aa
  (def ab (.getScaledPath aa -2400))
  (def ab (j/get aa :path))
  ab
  (.toSVG (.scale (.scale ab -1 1) (glyph-scale :white 12)))
  (.toSVG ab)
  (.toSVG (.rotate (.scale (.scale ab -1 1) (glyph-scale :white 12)) 90))
  (.toSVG (.scale (.scale ab -1 1) (glyph-scale :white 120)))

  (.toSVG (.scale ab (- (glyph-scale :white 24)) (glyph-scale :white 24)))
  ;; (.toSVG)    )
  (.getScaledPath ab 12)
  (glyph-scale :white 24))

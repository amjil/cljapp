(ns app.ui.home.dragable
  (:require
    ["react-native-gesture-handler" :as gesture]
    ["react-native-reanimated" :as re-animated]
    ["react-native" :as rn]
    [reagent.core :as r]
    [goog.object :as goog-obj]))

(defn gget-in [m ks]
  (goog-obj/getValueByKeys m (clj->js (map name ks))))

(defn gkeys [obj]
  (goog-obj/getKeys obj))


(defn draggable-view-gesture []
  (let [x (new rn/Animated.Value 0)
        y (new rn/Animated.Value 0)
        last-offset (new rn/Animated.ValueXY #js {:x 0
                                                  :y 0})
        gesture-handler (rn/Animated.event (clj->js [{:nativeEvent {:translationX x
                                                                    :translationY y}}])
                                           (clj->js {:useNativeDriver true}))
        state-handler (fn [event]
                        (if (= (gget-in event [:nativeEvent :oldState])
                               gesture/State.ACTIVE)
                          (do
                            (.setValue ^js last-offset #js {:x (+ (gget-in last-offset [:x :_value]) (gget-in event [:nativeEvent :translationX]))
                                                            :y (+ (gget-in last-offset [:y :_value]) (gget-in event [:nativeEvent :translationY]))})

                            (.setOffset ^js x (gget-in last-offset [:x :_value]))
                            (.setOffset ^js y (gget-in last-offset [:y :_value]))

                            (.setValue ^js x 0)
                            (.setValue ^js y 0))))]
    (fn []
      [:> gesture/PanGestureHandler {:maxPointers 1
                                     :onGestureEvent gesture-handler
                                     :onHandlerStateChange state-handler}
       [:> rn/Animated.View {:style {:background-color "blue"
                                     :height 100
                                     :width 100
                                     :transform [{:translateX x}
                                                 {:translateY y}]}}]])))

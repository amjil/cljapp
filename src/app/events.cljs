(ns app.events
  (:require
   [re-frame.core :as re-frame]
   [app.db :as db]))


(re-frame/reg-event-fx                                      ;; usage: (dispatch [:initialise-app])
 :initialise-app                                            ;; gets user from localstore, and puts into coeffects arg
 ;; the event handler (function) being registered
 (fn [_ _]                                                  ;; take 2 vals from coeffects. Ignore event vector itself.
   {:db               db/default-db}))                         ;; what it returns becomes the new application state}))

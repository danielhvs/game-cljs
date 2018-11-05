(ns game-cljs.events
  (:require
   [re-frame.core :as re-frame]
   [game-cljs.db :as db]
   ))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(ns game-cljs.events
  (:require
   [re-frame.core :as rf]
   [game-cljs.db :as db]
   ))

(rf/reg-event-db
 ::initialize-db
 (fn [_ _]
   {:snake (db/cria-snake)}))

(rf/reg-event-db 
  :timer                  
  (fn [db [_ new-time]]   
    (assoc db :snake (db/move (:snake db)))
)) 


(ns game-cljs.subs
  (:require
   [re-frame.core :as rf]))

(rf/reg-sub :snake-estado (fn [db _] (when (:count db) 
                                       (nth (:rec-snake db) (:count db))))) 
(rf/reg-sub :maca-estado (fn [db _] (when (:count db) 
                                      (nth (:rec-maca db) (:count db))))) 
(rf/reg-sub :snake (fn [db _] (:snake db))) 
(rf/reg-sub :maca (fn [db _] (:maca db))) 
(rf/reg-sub :feedback (fn [db _] (:feedback db))) 

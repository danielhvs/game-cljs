(ns game-cljs.subs
  (:require
   [re-frame.core :as rf]))

(rf/reg-sub :snake-estado (fn [db _] (:snake (nth (:rec db) (:count db))))) 
(rf/reg-sub :maca-estado (fn [db _] (:maca (nth (:rec db) (:count db))))) 
(rf/reg-sub :snake (fn [db _] (:snake (:estado db)))) 
(rf/reg-sub :maca (fn [db _] (:maca (:estado db)))) 
(rf/reg-sub :feedback (fn [db _] (:feedback db))) 

import { createRouter, createWebHistory } from "vue-router";

import HomeView from "@/views/HomeView.vue";
import LoginView from "@/views/LoginView.vue";
import RegisterView from "@/views/RegisterView.vue";

import AppLayout from "@/layouts/AppLayout.vue";
import ProfileView from "@/views/ProfileView.vue";
import SessionsView from "@/views/SessionsView.vue";
import NotificationsView from "@/views/NotificationsView.vue";
import UsersView from "@/views/UsersView.vue";

import CreateSessionView from "@/views/CreateSessionView.vue";
import AdminGamesView from "@/views/AdminGamesView.vue";

const router = createRouter({
  history: createWebHistory(),
  routes: [
    // PUBLIC
    { path: "/", component: HomeView },
    { path: "/login", component: LoginView },
    { path: "/register", component: RegisterView },

    // APP (with navbar)
    {
      path: "/",
      component: AppLayout,
      children: [
        { path: "profile", component: ProfileView },
        { path: "sessions", component: SessionsView },
        { path: "notifications", component: NotificationsView },
        { path: "users", component: UsersView },
        { path: "sessions/create", component: CreateSessionView },
        {
          path: "admin/games",
          component: AdminGamesView,
        },
      ],
    },
  ],
});

export default router;

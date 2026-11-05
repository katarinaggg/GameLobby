import api from "./client";

/* =======================
   TIPOVI
======================= */

export type SessionType = "PUBLIC" | "PRIVATE";
export type SessionStatus = "SCHEDULED" | "CANCELED" | "CLOSED";

export type SessionResponse = {
  sessionId: number;
  name: string;
  gameId: number;
  gameName: string;
  organizerId: number;
  maxPlayers: number;
  sessionType: SessionType;
  startTime: string;
  description: string;
  status: SessionStatus;
  participantIds: number[];
  participants: any[];
};

export type CreateSessionPayload = {
  name: string;
  gameId: number;
  maxPlayers: number;
  sessionType: SessionType;
  startTime: string; // ISO
  description?: string;
};

export type CloseSessionPayload = {
  sessionId: number;
  participantsAttendance: Record<number, boolean>;
};

export type Game = {
  id: number;
  name: string;
  description?: string;
  genre: string;
};

/* =======================
   SESSIONS
======================= */

// GET /sessions
export async function getAllSessions() {
  const { data } = await api.get<SessionResponse[]>("/api/sessions");
  return data;
}

// GET /sessions/filtered
export async function getFilteredSessions(params: {
  gameId?: number;
  type?: SessionType;
  maxPlayers?: number;
  description?: string;
  userId?: number;
  sortBy?: "startTime" | "participantsCount";
}) {
  const { data } = await api.get<SessionResponse[]>("/api/sessions/filtered", {
    params,
  });
  return data;
}

// POST /sessions
export async function createSession(payload: CreateSessionPayload) {
  const { data } = await api.post<SessionResponse>("/api/sessions", payload);
  return data;
}

// POST /sessions/{id}/join
export async function joinSession(sessionId: number) {
  const { data } = await api.post<SessionResponse>(
    `/api/sessions/${sessionId}/join`
  );
  return data;
}

// POST /sessions/{id}/leave
export async function leaveSession(sessionId: number) {
  const { data } = await api.post<SessionResponse>(
    `/api/sessions/${sessionId}/leave`
  );
  return data;
}

// POST /sessions/{id}/cancel
export async function cancelSession(sessionId: number) {
  await api.post(`/api/sessions/${sessionId}/cancel`);
}

// POST /sessions/close
export async function closeSession(payload: CloseSessionPayload) {
  await api.post("/api/sessions/close", payload);
}

// POST /sessions/{id}/invite
export async function inviteUserToSession(
  sessionId: number,
  invitedUserId: number
) {
  await api.post(`/api/sessions/${sessionId}/invite`, null, {
    params: { invitedUserId },
  });
}

/* =======================
   GAMES (ADMIN)
======================= */

// GET /sessions/games
export async function getAllGames() {
  const { data } = await api.get<Game[]>("/api/sessions/games");
  return data;
}

// POST /sessions/games/create
export async function createGame(payload: Partial<Game>) {
  const { data } = await api.post<Game>("/api/sessions/games/create", payload);
  return data;
}

// PUT /sessions/games/update/{id}
export async function updateGame(id: number, payload: Partial<Game>) {
  const { data } = await api.put<Game>(
    `/api/sessions/games/update/${id}`,
    payload
  );
  return data;
}

// DELETE /sessions/games/delete/{id}
export async function deleteGame(id: number) {
  await api.delete(`/api/sessions/games/delete/${id}`);
}

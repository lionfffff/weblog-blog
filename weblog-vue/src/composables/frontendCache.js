const CACHE_PREFIX = 'weblog-cache:'

function buildKey(key) {
  return `${CACHE_PREFIX}${key}`
}

export function getCachedValue(key, maxAgeMs = 300000) {
  try {
    const raw = sessionStorage.getItem(buildKey(key))
    if (!raw) return null

    const parsed = JSON.parse(raw)
    if (!parsed || typeof parsed.timestamp !== 'number') return null

    if (Date.now() - parsed.timestamp > maxAgeMs) {
      sessionStorage.removeItem(buildKey(key))
      return null
    }

    return parsed.value ?? null
  } catch {
    return null
  }
}

export function setCachedValue(key, value) {
  try {
    sessionStorage.setItem(
      buildKey(key),
      JSON.stringify({
        timestamp: Date.now(),
        value,
      }),
    )
  } catch {
    // Ignore cache write failures.
  }
}

export function removeCachedValue(key) {
  try {
    sessionStorage.removeItem(buildKey(key))
  } catch {
    // Ignore cache delete failures.
  }
}

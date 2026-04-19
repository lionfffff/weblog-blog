import avatarFallback from '@/assets/avatar.png.jpg'

export const DEFAULT_AVATAR = avatarFallback
export const DEFAULT_LOGO = '/images/logo.svg'
export const DEFAULT_ARTICLE_COVER = '/images/cover-project.svg'

export function applyImageFallback(event, fallbackSrc) {
  const target = event?.target
  if (!target || target.dataset.fallbackApplied === 'true') return

  target.dataset.fallbackApplied = 'true'
  target.src = fallbackSrc
}

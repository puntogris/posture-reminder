const { description } = require('../../package')

module.exports = {
  /**
   * Ref：https://v1.vuepress.vuejs.org/config/#title
   */
  title: 'Posture reminder',
  /**
   * Ref：https://v1.vuepress.vuejs.org/config/#description
   */
  description: 'description',

  /**
   * Extra tags to be injected to the page HTML `<head>`
   *
   * ref：https://v1.vuepress.vuejs.org/config/#head
   */
  head: [
    ['meta', { name: 'theme-color', content: '#3eaf7c' }],
    ['meta', { name: 'apple-mobile-web-app-capable', content: 'yes' }],
    ['meta', { name: 'apple-mobile-web-app-status-bar-style', content: 'black' }]
  ],

  /**
   * Theme configuration, here is the default theme configuration for VuePress.
   *
   * ref：https://v1.vuepress.vuejs.org/theme/default-theme-config.html
   */
  themeConfig: {
    repo: '',
    logo: '/icons/logo.png',
    editLinks: false,
    docsDir: '',
    editLinkText: '',
    lastUpdated: false,
    nav: [
      {
        text: 'Home',
        link: '/index/',
      },
      {
        text: 'Versions',
        link: '/versions/',
      },
      {
        text: 'Help',
        link: '/help/'
      },
      {
        text: 'Contact',
        link: '/contact/'
      },
      {
        text: 'GitHub',
        link: 'https://github.com/puntogris/posture-reminder'
      }
    ],
    sidebar: {
      '/versions/': [
        {
          title: 'Versions',
          collapsable: false,
          children: [
            '',
            'v2.0.0', 
            'v2.0.0-a2', 
            'v2.0.0-a1',
            'v2.1.0-a1'
          ]
        }
      ],
      '/help/':[{
        'title': 'Help',
        collapsable: false,
        children: [
          ''
        ]
      }
      ],
      '/information/':[{
        'title': 'Information',
        collapsable: false,
        children: [
          '',
          'privacy-policy',
          'terms-and-conditions'
        ]
      }
    ]
    }
  },

  /**
   * Apply plugins，ref：https://v1.vuepress.vuejs.org/zh/plugin/
   */
  plugins: [
    '@vuepress/plugin-back-to-top',
    '@vuepress/plugin-medium-zoom',
    '@vuepress/back-to-top'
  ]
}

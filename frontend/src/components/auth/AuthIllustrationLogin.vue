<template>
  <div
    class="pointer-events-none absolute inset-0 select-none overflow-hidden"
    aria-hidden="true"
  >
    <svg
      class="h-full w-full object-cover md:object-contain md:object-left"
      viewBox="0 0 900 1000"
      fill="none"
      xmlns="http://www.w3.org/2000/svg"
      preserveAspectRatio="xMinYMid slice"
    >
      <defs>
        <!-- Soft Warm Gradients -->
        <linearGradient
          id="wallWash"
          x1="0"
          y1="0"
          x2="1"
          y2="1"
        >
          <stop
            offset="0%"
            stop-color="#faf7f2"
            stop-opacity="0.9"
          />
          <stop
            offset="100%"
            stop-color="#f5efe6"
            stop-opacity="0.4"
          />
        </linearGradient>

        <linearGradient
          id="archSunlight"
          x1="200"
          y1="50"
          x2="200"
          y2="700"
          gradientUnits="userSpaceOnUse"
        >
          <stop
            offset="0%"
            stop-color="#fff9ee"
            stop-opacity="0.8"
          />
          <stop
            offset="100%"
            stop-color="#f5ede0"
            stop-opacity="0.2"
          />
        </linearGradient>

        <linearGradient
          id="bookCoverTerracotta"
          x1="0"
          y1="0"
          x2="1"
          y2="1"
        >
          <stop
            offset="0%"
            stop-color="#b84a1d"
          />
          <stop
            offset="50%"
            stop-color="#9a3b12"
          />
          <stop
            offset="100%"
            stop-color="#7a2a07"
          />
        </linearGradient>

        <linearGradient
          id="bookCoverGrey"
          x1="0"
          y1="0"
          x2="0"
          y2="1"
        >
          <stop
            offset="0%"
            stop-color="#ded8d0"
          />
          <stop
            offset="100%"
            stop-color="#c5bdb4"
          />
        </linearGradient>

        <linearGradient
          id="lampBronze"
          x1="0"
          y1="0"
          x2="1"
          y2="1"
        >
          <stop
            offset="0%"
            stop-color="#4a4744"
          />
          <stop
            offset="50%"
            stop-color="#33312e"
          />
          <stop
            offset="100%"
            stop-color="#242220"
          />
        </linearGradient>

        <linearGradient
          id="lampGlow"
          x1="120"
          y1="520"
          x2="180"
          y2="780"
          gradientUnits="userSpaceOnUse"
        >
          <stop
            offset="0%"
            stop-color="#ffeacc"
            stop-opacity="0.35"
          />
          <stop
            offset="60%"
            stop-color="#fdf3e7"
            stop-opacity="0.1"
          />
          <stop
            offset="100%"
            stop-color="#ffffff"
            stop-opacity="0"
          />
        </linearGradient>

        <filter
          id="softShadow"
          x="-10%"
          y="-10%"
          width="130%"
          height="130%"
        >
          <feDropShadow
            dx="2"
            dy="8"
            stdDeviation="6"
            flood-color="#543c2c"
            flood-opacity="0.12"
          />
        </filter>
      </defs>

      <!-- 1. Background Architectural Structure (Arched Wall & Window) -->
      <g
        stroke="#dcd3c8"
        stroke-width="1.2"
        stroke-linecap="round"
        stroke-linejoin="round"
        opacity="0.85"
      >
        <!-- Main Tall Arch -->
        <path
          d="M 90 650 L 90 320 C 90 160 270 160 270 320 L 270 650"
          fill="url(#archSunlight)"
        />
        <path
          d="M 105 650 L 105 325 C 105 180 255 180 255 325 L 255 650"
          stroke="#e8dfd5"
          fill="none"
        />
        
        <!-- Arch Window Muntins / Panes -->
        <line
          x1="180"
          y1="180"
          x2="180"
          y2="650"
          stroke="#ded4c8"
          stroke-width="1"
        />
        <path
          d="M 105 320 C 130 260 230 260 255 320"
          stroke="#ded4c8"
          stroke-width="1"
          fill="none"
        />
        <line
          x1="90"
          y1="420"
          x2="270"
          y2="420"
          stroke="#e5ddd3"
          stroke-width="1"
        />
        <line
          x1="90"
          y1="530"
          x2="270"
          y2="530"
          stroke="#e5ddd3"
          stroke-width="1"
        />

        <!-- Distant Architectural Moulding Lines -->
        <line
          x1="0"
          y1="140"
          x2="450"
          y2="140"
          stroke="#e6dfd7"
          stroke-width="1.5"
        />
        <line
          x1="0"
          y1="150"
          x2="450"
          y2="150"
          stroke="#ece5dd"
          stroke-width="1"
        />
      </g>

      <!-- 2. Giant Bookshelves Line Art (Left Side) -->
      <g
        stroke="#c7bbb0"
        stroke-width="1.2"
        stroke-linecap="round"
        stroke-linejoin="round"
        opacity="0.9"
      >
        <!-- Uprights -->
        <line
          x1="20"
          y1="150"
          x2="20"
          y2="700"
          stroke-width="2"
        />
        <line
          x1="85"
          y1="150"
          x2="85"
          y2="700"
          stroke-width="1.5"
        />

        <!-- Shelf 1 (Top) -->
        <rect
          x="20"
          y="240"
          width="65"
          height="6"
          fill="#f0eae1"
        />
        <!-- Books on Shelf 1 -->
        <rect
          x="24"
          y="180"
          width="7"
          height="60"
          fill="#eae3da"
        />
        <rect
          x="33"
          y="175"
          width="9"
          height="65"
          fill="#dfd6cb"
        />
        <rect
          x="44"
          y="190"
          width="6"
          height="50"
          fill="#e5ded4"
        />
        <rect
          x="52"
          y="170"
          width="11"
          height="70"
          fill="#d9cfc3"
        />
        <rect
          x="65"
          y="185"
          width="8"
          height="55"
          fill="#e8e1d7"
        />
        <path
          d="M 75 195 L 81 240 L 77 240 L 73 195 Z"
          fill="#dfd5ca"
        />

        <!-- Shelf 2 -->
        <rect
          x="20"
          y="340"
          width="65"
          height="6"
          fill="#f0eae1"
        />
        <!-- Books on Shelf 2 -->
        <rect
          x="24"
          y="270"
          width="12"
          height="70"
          fill="#d8cec2"
        />
        <rect
          x="38"
          y="285"
          width="8"
          height="55"
          fill="#eae2d8"
        />
        <rect
          x="48"
          y="275"
          width="6"
          height="65"
          fill="#e2d9cd"
        />
        <rect
          x="56"
          y="265"
          width="14"
          height="75"
          fill="#d3c7bb"
        />
        <rect
          x="72"
          y="280"
          width="8"
          height="60"
          fill="#e5ddd3"
        />

        <!-- Shelf 3 -->
        <rect
          x="20"
          y="440"
          width="65"
          height="6"
          fill="#f0eae1"
        />
        <!-- Books on Shelf 3 -->
        <rect
          x="24"
          y="380"
          width="10"
          height="60"
          fill="#e0d7cb"
        />
        <rect
          x="36"
          y="370"
          width="14"
          height="70"
          fill="#d5cabb"
        />
        <rect
          x="52"
          y="390"
          width="7"
          height="50"
          fill="#eae3d9"
        />
        <rect
          x="61"
          y="375"
          width="12"
          height="65"
          fill="#ded4c8"
        />
        <rect
          x="75"
          y="385"
          width="7"
          height="55"
          fill="#d8cec1"
        />

        <!-- Shelf 4 -->
        <rect
          x="20"
          y="540"
          width="65"
          height="6"
          fill="#f0eae1"
        />
        <!-- Books on Shelf 4 -->
        <rect
          x="24"
          y="470"
          width="8"
          height="70"
          fill="#ded5c9"
        />
        <rect
          x="34"
          y="480"
          width="11"
          height="60"
          fill="#d4c8bc"
        />
        <rect
          x="47"
          y="465"
          width="14"
          height="75"
          fill="#e6ded4"
        />
        <rect
          x="63"
          y="490"
          width="8"
          height="50"
          fill="#dacfc3"
        />
        <rect
          x="73"
          y="475"
          width="9"
          height="65"
          fill="#dfd6cb"
        />
      </g>

      <!-- 3. Desk Surface in Foreground -->
      <g>
        <!-- Desk Horizon Lines -->
        <path
          d="M 0 660 L 450 660"
          stroke="#bdaea1"
          stroke-width="2.5"
          stroke-linecap="round"
        />
        <path
          d="M 0 670 L 450 670"
          stroke="#d5c8bb"
          stroke-width="1"
        />
        
        <!-- Wood Surface Tone -->
        <polygon
          points="0,661 450,661 450,850 0,850"
          fill="#f9f5ef"
          opacity="0.6"
        />
        
        <!-- Subtle Wood Grain Streaks -->
        <line
          x1="30"
          y1="690"
          x2="220"
          y2="690"
          stroke="#e8dfd5"
          stroke-width="1"
          stroke-linecap="round"
        />
        <line
          x1="260"
          y1="710"
          x2="420"
          y2="710"
          stroke="#ebe2d9"
          stroke-width="1"
          stroke-linecap="round"
        />
        <line
          x1="80"
          y1="740"
          x2="310"
          y2="740"
          stroke="#ece3da"
          stroke-width="1"
          stroke-linecap="round"
        />
        <line
          x1="0"
          y1="780"
          x2="200"
          y2="780"
          stroke="#eee5dc"
          stroke-width="1"
          stroke-linecap="round"
        />
      </g>

      <!-- Light Cone from Lamp onto Desk -->
      <polygon
        points="120,530 0,850 320,850 135,535"
        fill="url(#lampGlow)"
      />

      <!-- 4. Stack of Hardcover Books on Desk -->
      <g filter="url(#softShadow)">
        <!-- Lower Book (Grey / Light Canvas) -->
        <g
          stroke="#2f2d2b"
          stroke-width="1.8"
          stroke-linecap="round"
          stroke-linejoin="round"
        >
          <!-- Book base & pages -->
          <polygon
            points="40,715 140,685 155,720 55,750"
            fill="#faf7f0"
          />
          <polygon
            points="35,710 135,680 150,715 50,745"
            fill="url(#bookCoverGrey)"
          />
          <!-- Page edges thickness -->
          <polygon
            points="50,745 150,715 150,735 50,765"
            fill="#f5f0e6"
            stroke="#4a4642"
            stroke-width="1.2"
          />
          <!-- Page lines on edge -->
          <line
            x1="53"
            y1="751"
            x2="147"
            y2="723"
            stroke="#d5ccbe"
            stroke-width="0.8"
          />
          <line
            x1="53"
            y1="757"
            x2="147"
            y2="729"
            stroke="#d5ccbe"
            stroke-width="0.8"
          />
        </g>

        <!-- Upper Book (Terracotta Bound with Gold Details) -->
        <g
          stroke="#261b14"
          stroke-width="1.8"
          stroke-linecap="round"
          stroke-linejoin="round"
        >
          <!-- Book Top Cover -->
          <polygon
            points="32,695 130,665 146,700 48,730"
            fill="url(#bookCoverTerracotta)"
          />
          
          <!-- Book Spine Left/Front -->
          <polygon
            points="30,695 48,730 48,746 30,711"
            fill="#7a2a07"
          />
          
          <!-- Book Page Block Rim -->
          <polygon
            points="48,730 146,700 146,716 48,746"
            fill="#fdfaf4"
          />
          <!-- Page block lines -->
          <line
            x1="51"
            y1="735"
            x2="143"
            y2="707"
            stroke="#e0d6c8"
            stroke-width="0.8"
          />
          <line
            x1="51"
            y1="740"
            x2="143"
            y2="712"
            stroke="#d5cabd"
            stroke-width="0.8"
          />
          
          <!-- Decorative Gold/Debossed Spine Ribs -->
          <line
            x1="34"
            y1="701"
            x2="44"
            y2="721"
            stroke="#e8b15d"
            stroke-width="1.4"
            stroke-linecap="round"
          />
          <line
            x1="38"
            y1="707"
            x2="48"
            y2="727"
            stroke="#e8b15d"
            stroke-width="1.4"
            stroke-linecap="round"
          />
          <line
            x1="42"
            y1="713"
            x2="52"
            y2="733"
            stroke="#e8b15d"
            stroke-width="1.4"
            stroke-linecap="round"
          />
          
          <!-- Subtle Cover Inset Border -->
          <polygon
            points="40,696 124,670 138,698 54,724"
            fill="none"
            stroke="#e8b15d"
            stroke-width="0.9"
            opacity="0.75"
          />
        </g>
      </g>

      <!-- 5. Vintage Architect's Desk Lamp -->
      <g
        stroke="#1e1d1c"
        stroke-linecap="round"
        stroke-linejoin="round"
      >
        <!-- Weighted Cast-Iron Circular Base -->
        <ellipse
          cx="90"
          cy="655"
          rx="24"
          ry="9"
          fill="url(#lampBronze)"
          stroke-width="1.8"
        />
        <ellipse
          cx="90"
          cy="651"
          rx="20"
          ry="7"
          fill="#4d4945"
          stroke-width="1.2"
        />
        <path
          d="M 70 655 C 70 660 110 660 110 655"
          fill="#242220"
          stroke-width="1.8"
        />

        <!-- Base Pivot Joint & Knobs -->
        <circle
          cx="90"
          cy="648"
          r="4.5"
          fill="#a87f48"
          stroke="#1e1d1c"
          stroke-width="1.4"
        />
        <circle
          cx="86"
          cy="648"
          r="2.5"
          fill="#d4aa6d"
          stroke="#1e1d1c"
          stroke-width="1"
        />

        <!-- Lower Articulated Arm (Double Steel Rods) -->
        <line
          x1="88"
          y1="645"
          x2="72"
          y2="548"
          stroke="url(#lampBronze)"
          stroke-width="2.5"
        />
        <line
          x1="93"
          y1="645"
          x2="77"
          y2="548"
          stroke="url(#lampBronze)"
          stroke-width="2.5"
        />

        <!-- Middle Pivot Elbow Joint & Tension Springs -->
        <circle
          cx="75"
          cy="548"
          r="6"
          fill="#a87f48"
          stroke="#1e1d1c"
          stroke-width="1.5"
        />
        <circle
          cx="75"
          cy="548"
          r="2.5"
          fill="#d4aa6d"
          stroke="#1e1d1c"
          stroke-width="1"
        />
        
        <!-- Upper Articulated Arm -->
        <line
          x1="75"
          y1="545"
          x2="118"
          y2="488"
          stroke="url(#lampBronze)"
          stroke-width="2.5"
        />
        <line
          x1="78"
          y1="549"
          x2="121"
          y2="492"
          stroke="url(#lampBronze)"
          stroke-width="2.5"
        />

        <!-- Head Swivel Joint & Wing Nut -->
        <circle
          cx="120"
          cy="490"
          r="5"
          fill="#a87f48"
          stroke="#1e1d1c"
          stroke-width="1.4"
        />

        <!-- Lamp Shade (Angled Bell Dome) -->
        <path
          d="M 116 486 L 108 518 C 108 528 142 542 148 532 L 132 498 Z"
          fill="url(#lampBronze)"
          stroke="#1e1d1c"
          stroke-width="1.8"
        />
        <!-- Inner Shade Rim & Bulb Glow -->
        <ellipse
          cx="128"
          cy="525"
          rx="19"
          ry="8"
          transform="rotate(-26 128 525)"
          fill="#fff3d6"
          stroke="#1e1d1c"
          stroke-width="1.4"
        />
        <ellipse
          cx="128"
          cy="525"
          rx="14"
          ry="5"
          transform="rotate(-26 128 525)"
          fill="#ffffff"
        />
      </g>
    </svg>
  </div>
</template>

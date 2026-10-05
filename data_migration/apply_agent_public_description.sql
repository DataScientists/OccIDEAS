-- Pre-fills AgentInfo.publicDescription from the agent descriptions in the drafted rule rationale
-- (apply_rule_rationale.sql / apply_probhigh_rationale.sql all start "You may have been exposed to <phrase>. ...").
-- These are drafts for expert review - edit them here, or later in the Public Description column on the Agents page.
-- Only fills agents with no publicDescription yet, so re-running it won't overwrite reviewed text.
-- Run addAgentPublicDescription.sql first.

-- 1,3 butadiene
UPDATE AgentInfo SET publicDescription = 'a chemical used in rubber and plastics manufacturing, classified as a known human carcinogen' WHERE idAgent = 72 AND (publicDescription IS NULL OR publicDescription = '');
-- Acid mists
UPDATE AgentInfo SET publicDescription = 'airborne droplets of acidic solutions, a respiratory and eye irritant' WHERE idAgent = 80 AND (publicDescription IS NULL OR publicDescription = '');
-- Acrylamide
UPDATE AgentInfo SET publicDescription = 'a chemical used in some manufacturing, classified as a probable human carcinogen and nerve toxin' WHERE idAgent = 78 AND (publicDescription IS NULL OR publicDescription = '');
-- Acrylates
UPDATE AgentInfo SET publicDescription = 'chemicals used in adhesives, paints and plastics, a skin and respiratory irritant and sensitiser' WHERE idAgent = 101 AND (publicDescription IS NULL OR publicDescription = '');
-- Ammoniacal compounds
UPDATE AgentInfo SET publicDescription = 'ammonia-based compounds, a respiratory and eye irritant' WHERE idAgent = 107 AND (publicDescription IS NULL OR publicDescription = '');
-- Arsenic
UPDATE AgentInfo SET publicDescription = 'a metal encountered in some industrial processes, classified as a known human carcinogen' WHERE idAgent = 55 AND (publicDescription IS NULL OR publicDescription = '');
-- Artificial UV
UPDATE AgentInfo SET publicDescription = 'ultraviolet light from artificial sources such as welding or UV lamps, a recognised cause of skin and eye damage including cancer with cumulative exposure' WHERE idAgent = 132 AND (publicDescription IS NULL OR publicDescription = '');
-- Asbestos, Asbestos Aust
UPDATE AgentInfo SET publicDescription = 'a mineral fibre once widely used in building materials, classified as a known human carcinogen' WHERE idAgent = 37 AND (publicDescription IS NULL OR publicDescription = '');
-- Asbestos, Asbestos Aust
UPDATE AgentInfo SET publicDescription = 'a mineral fibre once widely used in building materials, classified as a known human carcinogen' WHERE idAgent = 194 AND (publicDescription IS NULL OR publicDescription = '');
-- Benzene
UPDATE AgentInfo SET publicDescription = 'a solvent found in fuels and some industrial processes, classified as a known human carcinogen' WHERE idAgent = 4 AND (publicDescription IS NULL OR publicDescription = '');
-- Beryllium
UPDATE AgentInfo SET publicDescription = 'a metal used in some manufacturing and electronics, classified as a known human carcinogen and cause of chronic lung disease' WHERE idAgent = 56 AND (publicDescription IS NULL OR publicDescription = '');
-- Bushfire smoke
UPDATE AgentInfo SET publicDescription = 'smoke from bushfires, a complex mixture whose long-term health effects are still an active area of research' WHERE idAgent = 202 AND (publicDescription IS NULL OR publicDescription = '');
-- Cadmium
UPDATE AgentInfo SET publicDescription = 'a metal used in some manufacturing such as batteries or coatings, classified as a known human carcinogen' WHERE idAgent = 57 AND (publicDescription IS NULL OR publicDescription = '');
-- Carbamate
UPDATE AgentInfo SET publicDescription = 'a class of pesticide; evidence on long-term health effects varies by the specific compound' WHERE idAgent = 193 AND (publicDescription IS NULL OR publicDescription = '');
-- Carbon Disulphide
UPDATE AgentInfo SET publicDescription = 'a solvent used in some manufacturing, a recognised cause of serious nervous system and cardiovascular effects with sustained exposure' WHERE idAgent = 140 AND (publicDescription IS NULL OR publicDescription = '');
-- Carbon Monoxide
UPDATE AgentInfo SET publicDescription = 'a gas from combustion sources, capable of serious acute poisoning at high concentrations' WHERE idAgent = 139 AND (publicDescription IS NULL OR publicDescription = '');
-- Chromium VI
UPDATE AgentInfo SET publicDescription = 'a form of chromium used in some manufacturing such as plating or welding, classified as a known human carcinogen' WHERE idAgent = 58 AND (publicDescription IS NULL OR publicDescription = '');
-- Cobalt
UPDATE AgentInfo SET publicDescription = 'a metal used in some manufacturing such as hard-metal tooling or batteries, linked to lung disease and classified as a possible human carcinogen' WHERE idAgent = 59 AND (publicDescription IS NULL OR publicDescription = '');
-- Cotton Dust
UPDATE AgentInfo SET publicDescription = 'dust from raw cotton processing, a recognised cause of occupational respiratory disease (byssinosis)' WHERE idAgent = 14 AND (publicDescription IS NULL OR publicDescription = '');
-- Diesel Exhaust
UPDATE AgentInfo SET publicDescription = 'exhaust from diesel engines, classified as a known human carcinogen' WHERE idAgent = 31 AND (publicDescription IS NULL OR publicDescription = '');
-- diethyl/dimethyl sulphate
UPDATE AgentInfo SET publicDescription = 'reactive chemicals used in some manufacturing, classified as probable human carcinogens' WHERE idAgent = 75 AND (publicDescription IS NULL OR publicDescription = '');
-- Environmental Tobacco Smoke
UPDATE AgentInfo SET publicDescription = 'secondhand tobacco smoke, classified as a known human carcinogen' WHERE idAgent = 35 AND (publicDescription IS NULL OR publicDescription = '');
-- Epichlorhydrin
UPDATE AgentInfo SET publicDescription = 'a chemical used in some manufacturing such as epoxy resins, classified as a probable human carcinogen' WHERE idAgent = 74 AND (publicDescription IS NULL OR publicDescription = '');
-- Ethyl Benzene
UPDATE AgentInfo SET publicDescription = 'a solvent used in some manufacturing, classified as a possible human carcinogen' WHERE idAgent = 136 AND (publicDescription IS NULL OR publicDescription = '');
-- Ethylene oxide
UPDATE AgentInfo SET publicDescription = 'a gas used in sterilisation and some manufacturing, classified as a known human carcinogen' WHERE idAgent = 81 AND (publicDescription IS NULL OR publicDescription = '');
-- ETS incl vapes
UPDATE AgentInfo SET publicDescription = 'secondhand tobacco smoke or vape aerosol; tobacco smoke is a known human carcinogen, while evidence on vape aerosol is comparatively newer and still developing' WHERE idAgent = 204 AND (publicDescription IS NULL OR publicDescription = '');
-- Formaldehyde
UPDATE AgentInfo SET publicDescription = 'a chemical used in some manufacturing and building materials, classified as a known human carcinogen' WHERE idAgent = 152 AND (publicDescription IS NULL OR publicDescription = '');
-- Glyphosate
UPDATE AgentInfo SET publicDescription = 'a widely used herbicide; international health authorities differ on its cancer risk classification, and evidence is still debated' WHERE idAgent = 188 AND (publicDescription IS NULL OR publicDescription = '');
-- Grain Dust
UPDATE AgentInfo SET publicDescription = 'dust from grain handling, a recognised cause of occupational respiratory disease' WHERE idAgent = 13 AND (publicDescription IS NULL OR publicDescription = '');
-- Heat at work
UPDATE AgentInfo SET publicDescription = 'working in hot conditions, a recognised cause of heat stress and related illness, with risk depending on duration and intensity' WHERE idAgent = 205 AND (publicDescription IS NULL OR publicDescription = '');
-- Ionizing radiation
UPDATE AgentInfo SET publicDescription = 'radiation from sources such as X-rays or radioactive materials, classified as a known human carcinogen' WHERE idAgent = 47 AND (publicDescription IS NULL OR publicDescription = '');
-- Isocyanates
UPDATE AgentInfo SET publicDescription = 'chemicals used in some manufacturing such as spray painting or foam production, a leading cause of occupational asthma and a possible carcinogen' WHERE idAgent = 110 AND (publicDescription IS NULL OR publicDescription = '');
-- Lead
UPDATE AgentInfo SET publicDescription = 'a metal used in some manufacturing, a recognised cause of serious effects on the nervous system, kidneys and reproductive health' WHERE idAgent = 51 AND (publicDescription IS NULL OR publicDescription = '');
-- Leather dust
UPDATE AgentInfo SET publicDescription = 'dust from leather processing, linked to an increased risk of nasal cancer in some studies' WHERE idAgent = 16 AND (publicDescription IS NULL OR publicDescription = '');
-- Light at night
UPDATE AgentInfo SET publicDescription = 'exposure to light during normal sleeping hours, such as from night shift work, associated with circadian disruption; international health authorities classify shift work involving this as a probable carcinogen' WHERE idAgent = 64 AND (publicDescription IS NULL OR publicDescription = '');
-- Mercury
UPDATE AgentInfo SET publicDescription = 'a metal used in some industrial processes, a recognised cause of serious nervous system effects' WHERE idAgent = 133 AND (publicDescription IS NULL OR publicDescription = '');
-- Methyl bromide
UPDATE AgentInfo SET publicDescription = 'a fumigant gas, capable of serious acute toxicity and nervous system effects' WHERE idAgent = 192 AND (publicDescription IS NULL OR publicDescription = '');
-- Methylene chloride
UPDATE AgentInfo SET publicDescription = 'a solvent used in some manufacturing, classified as a probable human carcinogen' WHERE idAgent = 191 AND (publicDescription IS NULL OR publicDescription = '');
-- Mineral Oils
UPDATE AgentInfo SET publicDescription = 'petroleum-based oils used in manufacturing; historically linked to skin cancer with prolonged untreated exposure, though modern refined oils carry lower risk and evidence varies by oil type' WHERE idAgent = 24 AND (publicDescription IS NULL OR publicDescription = '');
-- MOCA
UPDATE AgentInfo SET publicDescription = 'a chemical used in some polyurethane manufacturing, classified as a probable human carcinogen' WHERE idAgent = 79 AND (publicDescription IS NULL OR publicDescription = '');
-- n-hexane
UPDATE AgentInfo SET publicDescription = 'a solvent used in some manufacturing, a recognised cause of nerve damage with prolonged exposure' WHERE idAgent = 137 AND (publicDescription IS NULL OR publicDescription = '');
-- Natural Oils
UPDATE AgentInfo SET publicDescription = 'naturally derived oils used in some processes; evidence on long-term health effects varies by source' WHERE idAgent = 26 AND (publicDescription IS NULL OR publicDescription = '');
-- Nickel
UPDATE AgentInfo SET publicDescription = 'a metal used in some manufacturing such as plating or welding, with some compounds classified as known human carcinogens' WHERE idAgent = 60 AND (publicDescription IS NULL OR publicDescription = '');
-- Nitrosamines
UPDATE AgentInfo SET publicDescription = 'chemicals that can form in some industrial processes, classified as probable human carcinogens' WHERE idAgent = 153 AND (publicDescription IS NULL OR publicDescription = '');
-- noise2
UPDATE AgentInfo SET publicDescription = 'occupational noise exposure, a recognised cause of hearing damage that depends on volume and duration' WHERE idAgent = 116 AND (publicDescription IS NULL OR publicDescription = '');
-- Ocular UV AU, Ocular UV EU
UPDATE AgentInfo SET publicDescription = 'ultraviolet light exposure to the eyes, a recognised cause of eye damage including cataracts' WHERE idAgent = 207 AND (publicDescription IS NULL OR publicDescription = '');
-- Ocular UV AU, Ocular UV EU
UPDATE AgentInfo SET publicDescription = 'ultraviolet light exposure to the eyes, a recognised cause of eye damage including cataracts' WHERE idAgent = 150 AND (publicDescription IS NULL OR publicDescription = '');
-- Organochlorines
UPDATE AgentInfo SET publicDescription = 'a class of chemicals including some older pesticides, several linked to cancer and hormonal effects, though evidence varies by specific compound' WHERE idAgent = 18 AND (publicDescription IS NULL OR publicDescription = '');
-- Organophosphates
UPDATE AgentInfo SET publicDescription = 'a class of pesticides, capable of serious acute nervous system effects' WHERE idAgent = 19 AND (publicDescription IS NULL OR publicDescription = '');
-- Ortho-toluidine
UPDATE AgentInfo SET publicDescription = 'a chemical used in some dye and rubber manufacturing, classified as a known human carcinogen' WHERE idAgent = 76 AND (publicDescription IS NULL OR publicDescription = '');
-- Other PAHs
UPDATE AgentInfo SET publicDescription = 'polycyclic aromatic hydrocarbons formed by combustion, such as in coal, tar or soot, several classified as known or probable human carcinogens' WHERE idAgent = 34 AND (publicDescription IS NULL OR publicDescription = '');
-- Other Pesticides
UPDATE AgentInfo SET publicDescription = 'pesticide chemicals; evidence on long-term health effects varies significantly by specific compound' WHERE idAgent = 22 AND (publicDescription IS NULL OR publicDescription = '');
-- Other Reactive Chemicals
UPDATE AgentInfo SET publicDescription = 'reactive chemicals, generally recognised as skin, eye or respiratory irritants' WHERE idAgent = 111 AND (publicDescription IS NULL OR publicDescription = '');
-- p-xylene
UPDATE AgentInfo SET publicDescription = 'a solvent found in fuels, such as during vehicle refuelling, and some industrial processes; it is not classified as a carcinogen, and evidence on health effects at low, brief exposure levels is still developing' WHERE idAgent = 135 AND (publicDescription IS NULL OR publicDescription = '');
-- PCBs
UPDATE AgentInfo SET publicDescription = 'polychlorinated biphenyls, industrial chemicals now banned in most countries, classified as known human carcinogens' WHERE idAgent = 151 AND (publicDescription IS NULL OR publicDescription = '');
-- Phase shift
UPDATE AgentInfo SET publicDescription = 'circadian disruption from shift work involving changing shift times, classified by international health authorities as a probable carcinogen, alongside other health effects' WHERE idAgent = 65 AND (publicDescription IS NULL OR publicDescription = '');
-- Phenoxy Herbicides
UPDATE AgentInfo SET publicDescription = 'a class of herbicides; evidence on cancer risk is debated and varies by specific compound' WHERE idAgent = 20 AND (publicDescription IS NULL OR publicDescription = '');
-- Shiftwork
UPDATE AgentInfo SET publicDescription = 'working outside standard daytime hours, associated with circadian disruption; international health authorities classify shift work as a probable carcinogen, alongside other health effects' WHERE idAgent = 203 AND (publicDescription IS NULL OR publicDescription = '');
-- Silica
UPDATE AgentInfo SET publicDescription = 'crystalline silica dust from cutting, grinding or drilling into materials like concrete, classified as a known human carcinogen and cause of silicosis' WHERE idAgent = 39 AND (publicDescription IS NULL OR publicDescription = '');
-- Sleep disturbances
UPDATE AgentInfo SET publicDescription = 'disrupted sleep patterns, often linked to shift work, associated with a range of health effects' WHERE idAgent = 66 AND (publicDescription IS NULL OR publicDescription = '');
-- Solar UV AU, Solar UV EU
UPDATE AgentInfo SET publicDescription = 'ultraviolet radiation from sunlight, classified as a known human carcinogen (skin cancer)' WHERE idAgent = 206 AND (publicDescription IS NULL OR publicDescription = '');
-- Solar UV AU, Solar UV EU
UPDATE AgentInfo SET publicDescription = 'ultraviolet radiation from sunlight, classified as a known human carcinogen (skin cancer)' WHERE idAgent = 131 AND (publicDescription IS NULL OR publicDescription = '');
-- Styrene
UPDATE AgentInfo SET publicDescription = 'a chemical used in plastics and resin manufacturing, classified as a possible human carcinogen' WHERE idAgent = 77 AND (publicDescription IS NULL OR publicDescription = '');
-- Synthetic Oils
UPDATE AgentInfo SET publicDescription = 'synthetic oils used in manufacturing; evidence on long-term health effects varies by formulation' WHERE idAgent = 25 AND (publicDescription IS NULL OR publicDescription = '');
-- Tetrachloroethylene (PERC)
UPDATE AgentInfo SET publicDescription = 'a solvent used in dry cleaning and some manufacturing, classified as a probable human carcinogen' WHERE idAgent = 10 AND (publicDescription IS NULL OR publicDescription = '');
-- Toluene
UPDATE AgentInfo SET publicDescription = 'a solvent used in some manufacturing, a recognised cause of nervous system effects and, with heavy exposure, reproductive harm' WHERE idAgent = 134 AND (publicDescription IS NULL OR publicDescription = '');
-- Trichloroethylene
UPDATE AgentInfo SET publicDescription = 'a solvent used in some manufacturing such as degreasing, classified as a known human carcinogen' WHERE idAgent = 9 AND (publicDescription IS NULL OR publicDescription = '');
-- Vibration
UPDATE AgentInfo SET publicDescription = 'hand-arm or whole-body vibration, a recognised cause of nerve and circulation damage that depends on intensity and duration' WHERE idAgent = 157 AND (publicDescription IS NULL OR publicDescription = '');
-- Vinyl chloride
UPDATE AgentInfo SET publicDescription = 'a chemical used in plastics manufacturing (PVC), classified as a known human carcinogen' WHERE idAgent = 73 AND (publicDescription IS NULL OR publicDescription = '');
-- Welding fumes
UPDATE AgentInfo SET publicDescription = 'fumes produced during welding, classified as a known human carcinogen' WHERE idAgent = 190 AND (publicDescription IS NULL OR publicDescription = '');
-- Wood Dust
UPDATE AgentInfo SET publicDescription = 'dust from wood processing, classified as a known human carcinogen, particularly from hardwoods' WHERE idAgent = 12 AND (publicDescription IS NULL OR publicDescription = '');
